package ru.egorodov.server

import org.json4s.{DefaultFormats, Formats}
import org.scalatra._
import org.scalatra.json._

import scala.concurrent.{ExecutionContext, Future}
import org.scalatra.FutureSupport
import _root_.akka.actor.{ActorSystem, Props}
import _root_.akka.pattern.ask
import ru.egorodov.server.actors.actions

class FilesController(system: ActorSystem) extends ScalatraServlet with JacksonJsonSupport with FutureSupport {
  protected implicit lazy val jsonFormats: Formats = DefaultFormats
  protected implicit def executor: ExecutionContext = system.dispatcher

  before() {
    contentType = formats("json")
  }

  get("/") {
    new AsyncResult() with ru.egorodov.server.implicits.Timeouts {
      override val is = {
        val filesActor = system.actorOf(Props(classOf[actors.FilesActor]))
        filesActor ? actors.FilesAll
      }
    }
  }

  get("/:filename") {
    new AsyncResult with ru.egorodov.server.implicits.Timeouts {
      override val is = {
        val supervisor = system.actorOf(Props(classOf[actors.EntryPoint]))
        supervisor ! actions.Job.Entry(s"./${params("filename")}")
        Future {
          Map("sdf" -> 10)
        }
      }
    }
  }
}