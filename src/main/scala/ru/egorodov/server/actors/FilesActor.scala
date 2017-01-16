package ru.egorodov.server.actors

import akka.actor.Actor
import ru.egorodov.server.files.FileRepository

case object FilesAll

class FilesActor extends Actor {
  override def receive: Receive = {
    case FilesAll => sender ! FileRepository.all(".")
  }
}
