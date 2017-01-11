package client.files
import client.Base
import org.scalajs.jquery.{JQueryAjaxSettings, JQueryXHR, jQuery => $, _}

import scala.scalajs.js

object FilesIndex extends Base {
  override def rootSelector: JQuery = $("#root-block")

  @scala.scalajs.js.annotation.JSExport
  def main(): Unit = {
    request(client.Main.main)
  }

  def render(htmlString: String) = {
    rootSelector.html(htmlString)
  }

  @js.native
  trait FileRepresentation extends js.Object {
    val filename: String
    val ext: String
    val createdAt: String
  }

  def request(callback: => Unit) = {
    $.ajax(js.Dynamic.literal(
      url = "/files",
      success = { (data: js.Any, textStatus: js.JSStringOps, jqXHR: JQueryXHR) =>
        val results = js.JSON.parse(jqXHR.responseText).asInstanceOf[js.Array[FileRepresentation]]
        render(new client.views.FilesView(scalatags.Text).get(results.toList))
        callback
      },
      error = { (jqXHR: JQueryXHR, textStatus: js.JSStringOps, errorThrow: js.JSStringOps) =>
        println(s"jqXHR=$jqXHR,text=$textStatus,err=$errorThrow")
      },
      `type` = "GET"
    ).asInstanceOf[JQueryAjaxSettings])
  }
}
