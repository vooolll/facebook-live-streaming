package client

import akka.http.scaladsl.Http
import akka.http.scaladsl.model.{HttpRequest, HttpResponse, Uri}
import akka.stream.ThrottleMode
import akka.stream.alpakka.sse.scaladsl.EventSource
import akka.stream.scaladsl.Sink
import config.FacebookConfig
import constants.FacebookUrls
import domain.oauth.FacebookAccessToken

import scala.concurrent.Future
import scala.concurrent.duration._

class FacebookStreamingClient(accessToken: FacebookAccessToken) {

  val resources = new ReactiveAppResources()

  import resources._

  def liveComments(liveVideoId: String) = {
    val uri = s"${FacebookUrls.baseUrl}/$liveVideoId/live_comments?" +
      s"access_token=${accessToken.tokenValue.value}&comment_rate=one_per_two_seconds&fields=from{id,name},message"

    val eventSource = EventSource(Uri(uri), send, Some("2"), 1.second)

    def send(request: HttpRequest): Future[HttpResponse] = {
      val result = Http().singleRequest(HttpRequest(uri = uri))
      result onComplete println
      result
    }

    eventSource.runForeach(serverEvent => println(serverEvent))

    val events = eventSource.throttle(1, 500.milliseconds, 5, ThrottleMode.Shaping).take(5).runWith(Sink.seq)
    events.onComplete(ev => println(ev))
  }
}

object FacebookStreamingClient {
  def apply(): FacebookStreamingClient = FacebookStreamingClient(FacebookConfig.accessToken)
  def apply(accessToken: FacebookAccessToken): FacebookStreamingClient = new FacebookStreamingClient(accessToken)
}