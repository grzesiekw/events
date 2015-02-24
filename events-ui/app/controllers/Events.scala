package controllers

import com.gw.events.Storage._
import com.gw.events.json.Parsers._
import main.Global.storage
import play.api.libs.json.{JsSuccess, Json}
import play.api.libs.json.Reads.list
import play.api.mvc.{Action, Controller}

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

object Events extends Controller {

  def index = Action {
    Ok(views.html.index())
  }

  def find = Action.async { request =>
    val attributesJson = request.body.asJson

    attributesJson.get.validate[List[EventAttribute]](list(eventAttributesReads)) match {
      case requestAttributes: JsSuccess[List[EventAttribute]] =>
        storage.findBy(requestAttributes.get).map(events => Ok(Json.toJson(events)))
      case _ =>
        Future {
          BadRequest
        }
    }
  }

}
