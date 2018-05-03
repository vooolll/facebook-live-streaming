package client

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.model.{HttpRequest, HttpResponse, Uri}
import akka.stream.{ActorMaterializer, ThrottleMode}
import akka.stream.alpakka.sse.scaladsl.EventSource
import akka.stream.scaladsl.Sink
import config.FacebookConfig
import domain.oauth.FacebookAccessToken

import scala.concurrent.Future
import scala.concurrent.duration._

class FacebookStreamingClient(accessToken: FacebookAccessToken)(implicit actorSystem: ActorSystem, materializer: ActorMaterializer) {
  val uri = "http://streaming-graph.facebook.com/202059110586785/live_comments?access_token=EAAcAL79ZCFjMBAOhVcq78FWl5i3RmZBSBTHEgjajCMmNW8Ypr7OB6HTEbBp4XqBkAQ20ZAeSkI6VtjN2fbXEWlJ5UyZAhfSVfpP6SOFTtMZBO04OexkNx9dJmDbAjcdQiH28zLKOZBYlsJG2NnJjoRp5oyF8aZCDYaUXrbhQWOz93nLYclvMZAZA2Skf5UDG482Gfe58SA5xz1gZDZD&comment_rate=one_per_two_seconds&fields=from{id,name},message"

  implicit val ec = actorSystem.dispatcher

  def send(request: HttpRequest): Future[HttpResponse] = {
    val result = Http().singleRequest(HttpRequest(uri = uri))
    result onComplete println
    result
  }
  val eventSource = EventSource(Uri(uri), send, Some("2"), 1.second)

  def liveComments() = {

    eventSource.runForeach(serverEvent => println(serverEvent))

    val events = eventSource.throttle(1, 500.milliseconds, 5, ThrottleMode.Shaping).take(5).runWith(Sink.seq)
    events.onComplete(ev => println(ev))
  }
}

object FacebookStreamingClient {
  def apply()(implicit actorSystem: ActorSystem, mat: ActorMaterializer): FacebookStreamingClient
    = FacebookStreamingClient(FacebookConfig.accessToken)(actorSystem, mat)
  def apply(accessToken: FacebookAccessToken)(implicit actorSystem: ActorSystem, mat: ActorMaterializer): FacebookStreamingClient
    = new FacebookStreamingClient(accessToken)(actorSystem, mat)
}