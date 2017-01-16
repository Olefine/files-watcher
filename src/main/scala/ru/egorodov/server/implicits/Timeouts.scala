package ru.egorodov.server.implicits

import _root_.akka.util.Timeout
import concurrent.duration._

trait Timeouts {
  implicit lazy val tm: Timeout = Timeout(10 seconds)
}
