package server.implicits

import _root_.akka.util.Timeout
import concurrent.duration._

object Timeouts {
  implicit lazy val timeout: Timeout = Timeout(10 seconds)
}
