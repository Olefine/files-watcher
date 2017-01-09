package client.files

import java.io.File

import client.Base
import org.scalajs.jquery.JQuery
import org.scalajs.jquery.{jQuery => $}
import server.decorators.FileDecorator

class FilesIndex(val selector: String) extends Base {
  class FilesView[Builder, Output <: FragT, FragT](bundle: scalatags.generic.Bundle[Builder, Output, FragT]) {
    def get(files: List[FileDecorator]): Output = {
      import bundle.all._
      div(
        files.map { file =>
          div(
            `class` := "file",
            `id` := s"file_${file.filename}",
            span(file.ext),
            span(file.filename),
            span(file.createdAt),
            a(`class` := "run-job")
          )
        } : _*
      ).render
    }
  }

  override def rootSelector: JQuery = $(selector)

  @scala.scalajs.js.annotation.JSExport
  def main(): Unit = {
    //TODO request files from "files/" and construct body of the page
  }

  def render = {
  }
}
