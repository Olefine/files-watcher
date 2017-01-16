package ru.egorodov.server.persistence

import scala.concurrent.Future

trait PersistenceBase {
  def create(entry: Future[Map[String, Int]])
}
