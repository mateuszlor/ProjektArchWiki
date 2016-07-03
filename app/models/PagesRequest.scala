package models

import play.api.libs.json.Json

object PagesRequest {

  case class PagesRequest(first: String, second: String)

  implicit val pagesRequestWrites = Json.writes[PagesRequest]
  implicit val pagesRequestReads = Json.reads[PagesRequest]

  var books = List(PagesRequest("TAOCP", "Knuth"), PagesRequest("SICP", "Sussman, Abelson"))

  def addBook(b: PagesRequest) = books = books ::: List(b)
}
