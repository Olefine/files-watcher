import javax.servlet.ServletContext

import org.scalatra._
import server.{FilesController, RootController}
import _root_.akka.actor.ActorSystem
import com.typesafe.config.ConfigFactory
import server.persistence.Database


class ScalatraBootstrap extends LifeCycle {
  val conf = ConfigFactory.load()
  val system = ActorSystem("files-watcher", conf.getConfig("application").withFallback(conf))

  override def init(context: ServletContext) {
    context.mount(new RootController(system), "/*")
    context.mount(new FilesController(system), "/files/*")

    system.actorOf(_root_.akka.actor.Props(classOf[server.actors.EntryPoint]), "root")
    Database
  }

  override def destroy(context: ServletContext): Unit = {
    system.terminate()
    Database.disconnect()
  }
}
