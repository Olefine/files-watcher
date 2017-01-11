package client.views

import client.files.FilesIndex.FileRepresentation

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
