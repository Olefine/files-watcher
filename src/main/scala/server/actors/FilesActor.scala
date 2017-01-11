package server.actors

import akka.actor.Actor
import server.files.FileRepository

case object FilesAll

class FilesActor extends Actor {
  override def receive: Receive = {
    case FilesAll => sender ! FileRepository.all(".")
  }
}
