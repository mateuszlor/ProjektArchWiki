package controllers

import play.api.libs.json._
import play.api.mvc._
import models.PagesRequest._
import main.scala._

object PagesRequests extends Controller {

  def checkPages = Action(BodyParsers.parse.json) { request =>
    val b = request.body.validate[PagesRequest]
    b.fold(
      errors => {
        BadRequest(Json.obj("status" -> "OK", "message" -> JsError.toFlatJson(errors)))
      },
      pages => {
        val first = b.map(p=>p.first).get
        val second = b.map(p=>p.second).get
        println(first)
        println(second)
        //val result = MainClass.play("https://wiki.archlinux.org/index.php/OpenRC", "https://wiki.archlinux.org/index.php/Persistent_block_device_naming_(Espa%C3%B1ol)")
        val result = MainClass.play(first,second)
        val pages = Json.obj("pages" -> result)

        Ok(pages)
      }
    )
  }
}
