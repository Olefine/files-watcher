package server.files

import java.io.File

import server.decorators.FileDecorator

object FileRepository {
  def all(dir: String) = {
    val d = new File(dir)
    if (d.exists && d.isDirectory) {
      d.listFiles.filter(_.isFile).toList.map(FileDecorator(_))
    } else {
      List[FileDecorator]()
    }
  }
}
