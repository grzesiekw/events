package com.gw.events

import org.joda.time.DateTime

import scala.concurrent.Future

trait Storage {
  import Storage._

  def store(event: AddEvent): Future[StoreResult]
  def update(event: UpdateEvent): Future[StoreResult]

  def findBy(attributes: List[EventAttribute]): Future[List[Event]]

}

object Storage {
  case class Event(id: String, description: String, generatedAt: DateTime, attributes: List[EventAttribute])
  case class EventAttribute(key: String, value: String)

  case class AddEvent(event: Event, childEvents: Option[List[Event]] = None)
  case class UpdateEvent(id: String, childEvents: List[Event])

  sealed trait StoreResult
  case object Stored extends StoreResult
  case class StoreFailed(cause: String) extends StoreResult

}

