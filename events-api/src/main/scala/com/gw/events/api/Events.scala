package com.gw.events.api

import akka.http.marshallers.sprayjson.SprayJsonSupport._
import akka.http.model.StatusCode
import akka.http.model.StatusCodes.{InternalServerError, OK}
import akka.http.server.Directives._
import akka.stream.FlowMaterializer
import akka.stream.scaladsl.Sink.head
import akka.stream.scaladsl.Source.single
import akka.stream.scaladsl._
import com.gw.events.Storage
import com.gw.events.Storage._
import org.joda.time.DateTime
import org.joda.time.format.DateTimeFormat
import spray.json._

import scala.concurrent.ExecutionContext

trait EventProtocol extends DefaultJsonProtocol {

  implicit val dateTimeFormat = new RootJsonFormat[DateTime] {
    private val formatter = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss")

    def write(obj: DateTime) = new JsString(formatter.print(obj))
    def read(json: JsValue) = {
      new DateTime(formatter.parseDateTime(json.convertTo[String]))
    }
  }

  implicit val eventAttributeFormat = jsonFormat2(EventAttribute)
  implicit val eventFormat = jsonFormat4(Event)
  implicit val addEventFormat = jsonFormat2(AddEvent)
  implicit val updateEventFormat = jsonFormat2(UpdateEvent)
}

trait EventRouter extends EventProtocol {
  val storeFlow: Flow[AddEvent, StoreResult]
  val updateFlow: Flow[UpdateEvent, StoreResult]

  implicit val executionContext: ExecutionContext
  implicit val materializer: FlowMaterializer

  private val storeResultToHttp: StoreResult => StatusCode = {
    case Stored => OK
    case StoreFailed(cause) => InternalServerError
  }

  val router = path("event") {
    (post & entity(as[AddEvent])) { event =>
      complete(single(event).via(storeFlow).runWith(head[StoreResult]).map(storeResultToHttp))
    } ~
    (patch & entity(as[UpdateEvent])) { event =>
      complete(single(event).via(updateFlow).runWith(head[StoreResult]).map(storeResultToHttp))
    }
  }
}

trait EventFlow {
  val storage: Storage

  val storeFlow = Flow[AddEvent].mapAsync(storage.store)
  val updateFlow = Flow[UpdateEvent].mapAsync(storage.update)
}