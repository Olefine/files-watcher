import javax.servlet.ServletContext

import org.scalatra._
import server.{FilesController, RootController}
import _root_.akka.actor.ActorSystem
import server.persistence.Database


class ScalatraBootstrap extends LifeCycle {
  val system = ActorSystem("FilesWatcherRoot")

  override def init(context: ServletContext) {
    context.mount(new RootController(system), "/*")
    context.mount(new FilesController(system), "/files/*")

    Database
  }

  override def destroy(context: ServletContext): Unit = {
    system.terminate()
    Database.disconnect()
  }
}
