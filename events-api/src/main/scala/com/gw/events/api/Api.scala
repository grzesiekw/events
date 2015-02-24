package com.gw.events.api

import akka.actor.ActorSystem
import akka.http.Http
import akka.http.server.Route.handlerFlow
import akka.stream.FlowMaterializer
import akka.util.Timeout
import com.gw.events.mongodb.MongoDbStorage
import com.typesafe.config.ConfigFactory
import reactivemongo.api.MongoDriver

import scala.concurrent.duration._

object Api extends App with EventRouter with EventFlow {

  val config = ConfigFactory.load()

  implicit val system = ActorSystem()
  implicit val executionContext = system.dispatcher

  implicit val materializer = FlowMaterializer()
  implicit val askTimeout: Timeout = 5000.millis

  val mongoDriver = MongoDriver(system)
  val storage = MongoDbStorage(mongoDriver, s"${config.getString("mongodb.host")}:${config.getString("mongodb.port")}")

  val binding = Http().bind(interface = config.getString("api.host"), port = config.getInt("api.port"))

  binding startHandlingWith handlerFlow {
    router
  }

  println("Up and running...")

}
