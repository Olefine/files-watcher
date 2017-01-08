package server.decorators

import java.io.File

/**
  * Created by egorgorodov on 1/8/17.
  */
object FileDecorator {
  def apply(_f: File): FileDecorator = new FileDecorator(_f)
}
class FileDecorator(_f: File) {
  private val f = _f

  def filename: String = f.getName
  def ext: String =
    if(filename.contains(".") && filename(0).toString.ne(".")) filename.split('.').tail.mkString(".")
    else "exec"

  def createdAt: String = java.nio.file.Files.getAttribute(f.toPath, "creationTime").toString
}
