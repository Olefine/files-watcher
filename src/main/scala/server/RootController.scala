package server

import org.scalatra._
import org.scalatra.scalate.ScalateSupport

import scala.concurrent.{ExecutionContext, Future}
import _root_.akka.actor.ActorSystem
import java.io.File

import server.decorators.FileDecorator

object GetFiles {
  def apply(dir: String): List[FileDecorator] = {
    val d = new File(dir)
    if (d.exists && d.isDirectory) {
      d.listFiles.filter(_.isFile).toList.map(FileDecorator(_))
    } else {
      List[FileDecorator]()
    }
  }
}
class RootController(system: ActorSystem) extends ScalatraServlet with ScalateSupport with FutureSupport {
  protected implicit def executor: ExecutionContext = system.dispatcher

  get("/") {
    contentType = "text/html"
    new AsyncResult() {
      override val is = Future {
        val files = GetFiles(".")
        ssp("/root/index", "files" -> files)
      }
    }
  }
}
