package client

import scalatags._

trait HasViewOf[A <: String] {
  def wrap: Text.TypedTag[String]
  def view: String
}
