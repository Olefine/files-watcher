package ru.egorodov.server

import org.scalatra._
import org.scalatra.scalate.ScalateSupport

import scala.concurrent.{ExecutionContext, Future}
import _root_.akka.actor.ActorSystem
import java.io.File

import ru.egorodov.server.decorators.FileDecorator

class RootController(system: ActorSystem) extends ScalatraServlet with ScalateSupport with FutureSupport {
  protected implicit def executor: ExecutionContext = system.dispatcher

  get("/") {
    contentType = "text/html"
    ssp("/root/index")
  }
}
