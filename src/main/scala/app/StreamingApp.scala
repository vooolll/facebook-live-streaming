package app

import akka.actor.ActorSystem
import akka.stream.ActorMaterializer
import client.FacebookStreamingClient
import domain.oauth.{FacebookAccessToken, TokenValue, UserAccessToken}

import scala.util.{Failure, Success}

object StreamingApp extends App {

  implicit val actorSystem = ActorSystem()
  implicit val materializer = ActorMaterializer()
  implicit val ec = actorSystem.dispatcher
  val token = "EAAcAL79ZCFjMBAHiFoLrfmPVpuZAwtNHd2PdGaQ4BSBiZCZBJACy3jiKEimNhava1JyOQbWOSUEbefM533wnyw9vaTDkgSG7tHROqjIFSC2o4fw1d7yqxYam9ZBu0yoZCan7kXRIhcqjZA8X0nLV7cXuaZA95234corjhN2LZAxlmCBk5L3pTEcBFZAHRrn7aSpZAbfJoyzEDFjRAZDZD"
  val client = FacebookStreamingClient(FacebookAccessToken(TokenValue(token), UserAccessToken.notSpecified))
  client.liveComments()
}
