package app

import akka.actor.ActorSystem
import akka.stream.ActorMaterializer
import client.FacebookStreamingClient
import domain.oauth.{FacebookAccessToken, TokenValue, UserAccessToken}

import scala.concurrent.Await
import scala.concurrent.duration.Duration

object StreamingApp extends App {

  implicit val actorSystem = ActorSystem()
  implicit val materializer = ActorMaterializer()
  val token = "EAAcAL79ZCFjMBAHiFoLrfmPVpuZAwtNHd2PdGaQ4BSBiZCZBJACy3jiKEimNhava1JyOQbWOSUEbefM533wnyw9vaTDkgSG7tHROqjIFSC2o4fw1d7yqxYam9ZBu0yoZCan7kXRIhcqjZA8X0nLV7cXuaZA95234corjhN2LZAxlmCBk5L3pTEcBFZAHRrn7aSpZAbfJoyzEDFjRAZDZD"
  val client = FacebookStreamingClient(FacebookAccessToken(TokenValue(token), UserAccessToken.notSpecified))
  Await.result(client.liveComments(), Duration.Inf)
}
