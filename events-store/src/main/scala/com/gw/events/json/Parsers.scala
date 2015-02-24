package com.gw.events.json

import org.joda.time.DateTime
import play.api.libs.json.Reads._
import play.api.libs.json._
import play.api.libs.functional.syntax._

import com.gw.events._

object Parsers {
  import Storage._

  val eventAttributesReads: Reads[EventAttribute] = (
    (JsPath \ "key").read[String] and (JsPath \ "value").read[String]
    )(EventAttribute)

  val eventReads: Reads[Event] = (
    (JsPath \ "id").read[String] and
      (JsPath \ "description").read[String] and
      (JsPath \ "generatedAt").read[DateTime] and
      (JsPath \ "attributes").lazyRead(list[EventAttribute](eventAttributesReads))
    )(Event)

  val parseAddEvent: String => Option[AddEvent] = {
    implicit val addEventReads: Reads[AddEvent] = (
      JsPath.read[Event](eventReads) and
        (JsPath \ "childEvents").readNullable(list[Event](eventReads))
      )(AddEvent)

    (data: String) => Json.parse(data).validate[AddEvent] match {
      case JsSuccess(event, _) => Some(event)
      case JsError(errors) =>
        println(errors)
        None
    }
  }

  val parseUpdateEvent: String => Option[UpdateEvent] = {
    implicit val updateEventReads: Reads[UpdateEvent] = (
      (JsPath \ "id").read[String] and
        (JsPath \ "childEvents").read(list[Event](eventReads))
      )(UpdateEvent)

    (data: String) => Json.parse(data).validate[UpdateEvent] match {
      case JsSuccess(event, _) => Some(event)
      case JsError(errors) =>
        println(errors)
        None
    }
  }

  implicit val eventAttributeWrites: Writes[EventAttribute] = (
    (JsPath \ "key").write[String] and
      (JsPath \ "value").write[String]
    )(unlift(EventAttribute.unapply))

  implicit val eventsWrites: Writes[Event] = (
    (JsPath \ "id").write[String] and
      (JsPath \ "description").write[String] and
      (JsPath \ "generatedAt").write[DateTime] and
      (JsPath \ "attributes").write(Writes.seq(eventAttributeWrites))
    )(unlift(Event.unapply))

}
