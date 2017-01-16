package ru.egorodov.server.persistence

import org.mongodb.scala.MongoClient

case object Database {
  var _conn: MongoClient = MongoClient()

  def database = _conn.getDatabase("files-watcher")
  def disconnect() = _conn.close()
}

