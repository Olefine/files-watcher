package ru.egorodov.server.actors

import akka.actor.Actor
import dispatch.Future
import ru.egorodov.server.persistence.WordsCountRepository

case class Create(dataToSave: Future[Map[String, Int]])

class PersistentActor extends Actor {
  override def receive: Receive = {
    case Create(dataToSave) => WordsCountRepository.create(dataToSave)
  }
}
