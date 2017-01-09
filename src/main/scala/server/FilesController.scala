package server
import server.files.FileRepository

import org.json4s.{DefaultFormats, Formats}
import org.scalatra._
import org.scalatra.json._

import scala.concurrent.{ExecutionContext, Future}
import org.scalatra.FutureSupport
import _root_.akka.actor.{ActorSystem, Props}
import _root_.akka.pattern.ask
import _root_.akka.util.Timeout

import concurrent.duration._

class FilesController(system: ActorSystem) extends ScalatraServlet with JacksonJsonSupport with FutureSupport {
  protected implicit lazy val jsonFormats: Formats = DefaultFormats
  protected implicit def executor: ExecutionContext = system.dispatcher

  before() {
    contentType = formats("json")
  }

  get("/") {
    new AsyncResult() {
      override val is = Future {
        //TODO rewrite with actor
        Map("files" -> FileRepository.all("."))
      }
    }
  }

  get("/:filename") {
    new AsyncResult {
      override val is = {
        implicit lazy val timeout: Timeout = Timeout(10 seconds)
        val supervisor = system.actorOf(Props(classOf[actors.CountSuperVisor], s"./${params("filename")}"))
        supervisor ? actors.Start
      }
    }
  }
}