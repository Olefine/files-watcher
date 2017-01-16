package ru.egorodov.server.files

import java.io.File

case class FileInfo(fileLink: String) {
  private val _file = new File(fileLink)

  def size: Long = java.nio.file.Files.size(_file.toPath)
}
