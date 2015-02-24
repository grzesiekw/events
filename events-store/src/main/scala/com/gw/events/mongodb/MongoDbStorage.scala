package com.gw.events.mongodb

import com.gw.events.Storage.Event
import com.gw.events._
import org.joda.time.DateTime
import reactivemongo.api.collections.default.BSONCollection
import reactivemongo.api.{MongoConnection, MongoDriver}
import reactivemongo.bson.{BSONArray, BSONDateTime, BSONDocument, BSONDocumentReader}

import scala.concurrent.ExecutionContext.Implicits.global

class MongoDbStorage(driver: MongoDriver, host: String, dbName: String, collectionName: String) extends Storage {
  import Storage._
  import MongoDbStorage.eventReader

  private val connection: MongoConnection = driver.connection(List(host), nbChannelsPerNode = 20)

  def store(addEvent: AddEvent) = {
    val doc = eventDocument(addEvent.event, addEvent.childEvents.getOrElse(List()))

    collection.save(doc).map(lastError => {println("store done"); if (lastError.ok) Stored else StoreFailed("") })
  }

  def update(updateEvent: UpdateEvent) = {
    collection.update(
      BSONDocument("id" -> updateEvent.id),
      BSONDocument("$push" ->
        BSONDocument("childEvents" -> BSONDocument("$each" -> BSONArray(updateEvent.childEvents.map(event => eventDocument(event)))))
      )
    ).map(lastError => {println("update done"); if (lastError.ok) Stored else StoreFailed("") })
  }

  def findBy(attributes: List[EventAttribute]) = {
    val query = BSONDocument("$and" -> BSONArray(attributes.map(attribute => BSONDocument("attributes" -> BSONDocument(attribute.key -> attribute.value)))))

    collection.find(query).cursor[Event].collect[List]()
  }

  private def collection: BSONCollection = {
    connection.db(dbName).collection[BSONCollection](collectionName)
  }

  private def eventDocument(event: Event, childEvents: List[Event] = List()): BSONDocument = {
    val doc = BSONDocument(
      "id" -> event.id,
      "description" -> event.description,
      "generatedAt" -> BSONDateTime(event.generatedAt.getMillis),
      "attributes" -> BSONArray(event.attributes.map(attribute => BSONDocument(attribute.key -> attribute.value)))
    )

    if (childEvents.nonEmpty)
      doc.add("childEvents" -> BSONArray(childEvents.map(event => eventDocument(event))))
    else
      doc
  }
}

object MongoDbStorage {
  implicit val eventReader: BSONDocumentReader[Event] = new BSONDocumentReader[Event] {
    def read(doc: BSONDocument) = {
      val id = doc.getAs[String]("id").get
      val description = doc.getAs[String]("description").get
      val generatedAt = new DateTime(doc.getAs[BSONDateTime]("generatedAt").get.value)

      Event(id, description, generatedAt, List())
    }
  }

  def apply(driver: MongoDriver, host: String = "localhost:27017", dbName: String = "events", collectionName: String = "default") =
    new MongoDbStorage(driver, host, dbName, collectionName)
}
