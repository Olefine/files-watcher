package server

import org.scalatra._
import org.scalatra.scalate.ScalateSupport
/**
  * Created by egorgorodov on 1/6/17.
  */
class RootController extends ScalatraServlet with ScalateSupport {
  get("/") {
    contentType = "text/html"
    ssp("/root/index")
  }
}
