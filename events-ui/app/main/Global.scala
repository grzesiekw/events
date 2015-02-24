package main

import com.gw.events.Storage
import com.gw.events.mongodb.MongoDbStorage
import play.api.{Logger, Application, GlobalSettings}

import play.api.libs.concurrent.Akka
import reactivemongo.api.MongoDriver

object Global extends GlobalSettings {

  var storage: Storage = _

  override def onStart(app: Application) = {
    val system = Akka.system(app)
    val mongoDriver = MongoDriver(system)

    val config = app.configuration

    val url = s"${config.getString("mongodb.host").get}:${config.getString("mongodb.port").get}"
    Logger.debug(s"Mongo url: $url")
    storage = MongoDbStorage(mongoDriver, url)
  }
}
