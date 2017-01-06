package server

import org.json4s.{DefaultFormats, Formats}
import org.scalatra._
import org.scalatra.json._

class FilesController extends ScalatraServlet with JacksonJsonSupport {
  protected implicit lazy val jsonFormats: Formats = DefaultFormats

  before() {
    contentType = formats("json")
  }

  get("/") {
    Map("hello" -> "world")
  }
}