import javax.servlet.ServletContext

import org.scalatra._
import server.{FilesController, RootController}

class ScalatraBootstrap extends LifeCycle {
  override def init(context: ServletContext) {
    context.mount(new RootController, "/*")
    context.mount(new FilesController, "/files/*")
  }
}
