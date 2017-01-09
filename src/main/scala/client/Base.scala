package client

import org.scalajs.jquery.JQuery
import scala.scalajs.js.JSApp

abstract class Base extends JSApp {
  def rootSelector: JQuery
  def render: String
}
