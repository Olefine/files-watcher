package client.files

import java.io.File

import client.{Base, Entry}
import org.scalajs.jquery.{JQueryAjaxSettings, JQueryXHR, jQuery => $, _}
import server.decorators.FileDecorator

import scala.scalajs.js

object FilesIndex extends Base {
  class FilesView[Builder, Output <: FragT, FragT](bundle: scalatags.generic.Bundle[Builder, Output, FragT]) {
    def get(files: List[FileRepresentation]): Output = {
      import bundle.all._
      div(
        files.map { file =>
          div(
            `class` := "file",
            `id` := s"file_${file.filename}",
            span(file.ext),
            span(file.filename),
            span(file.createdAt),
            a(`class` := "run-job", "play")
          )
        } : _*
      ).render
    }
  }

  override def rootSelector: JQuery = $("")

  @scala.scalajs.js.annotation.JSExport
  def main(): Unit = {
    request
    //TODO request files from "files/" and construct body of the page
  }

  def render = {
    ""
  }

  @js.native
  trait FileRepresentation extends js.Object {
    val filename: String
    val ext: String
    val createdAt: String
  }

  def request = {
    $.ajax(js.Dynamic.literal(
      url = "/files",
      success = { (data: js.Any, textStatus: js.JSStringOps, jqXHR: JQueryXHR) =>
        val results = js.JSON.parse(jqXHR.responseText).asInstanceOf[js.Array[FileRepresentation]]
        val resultHtml = new FilesView(scalatags.Text).get(results.toList)

        $("#root-block").html(resultHtml)
      },
      error = { (jqXHR: JQueryXHR, textStatus: js.JSStringOps, errorThrow: js.JSStringOps) =>
        println(s"jqXHR=$jqXHR,text=$textStatus,err=$errorThrow")
      },
      `type` = "GET"
    ).asInstanceOf[JQueryAjaxSettings])
  }
}
