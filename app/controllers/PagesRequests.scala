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
      book => {
        // TODO app logic

        var app = new MainClass()
        app.play("https://wiki.archlinux.org/index.php/OpenRC", "https://wiki.archlinux.org/index.php/Persistent_block_device_naming_(Espa%C3%B1ol)")

        // end logic

        // To delete

        val pagesList = List("strona 1", "strona 2", "strona 3")

        val pages = Json.obj("pages" -> pagesList)

        Ok(pages)
      }
    )
  }
}
