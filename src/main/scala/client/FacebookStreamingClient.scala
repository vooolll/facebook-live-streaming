package client

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.model.{HttpRequest, HttpResponse, Uri}
import akka.stream.ActorMaterializer
import akka.stream.alpakka.sse.scaladsl.EventSource
import config.FacebookConfig
import domain.oauth.FacebookAccessToken

import scala.concurrent.Future
import scala.concurrent.duration._

class FacebookStreamingClient(accessToken: FacebookAccessToken)(implicit actorSystem: ActorSystem, materializer: ActorMaterializer) {
  val uri = "streaming-graph.facebook.com/193632854762744/live_comments"

  def send(request: HttpRequest): Future[HttpResponse] = Http().singleRequest(HttpRequest(uri = uri))
  val eventSource = EventSource(Uri(uri), send, Some("2"), 1.second)


  def liveComments() = {
    eventSource.runForeach(e => println(e.data))(materializer)
  }
}

object FacebookStreamingClient {
  def apply()(implicit actorSystem: ActorSystem, mat: ActorMaterializer): FacebookStreamingClient
    = FacebookStreamingClient(FacebookConfig.accessToken)(actorSystem, mat)
  def apply(accessToken: FacebookAccessToken)(implicit actorSystem: ActorSystem, mat: ActorMaterializer): FacebookStreamingClient
    = new FacebookStreamingClient(accessToken)(actorSystem, mat)
}