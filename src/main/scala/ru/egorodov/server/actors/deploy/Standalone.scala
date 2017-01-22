package ru.egorodov.server.actors.deploy

import akka.actor.Actor.Receive
import akka.actor.{Actor, ActorLogging, ActorRef}

import scala.concurrent.Future
import scala.util.{Failure, Success}

class Standalone extends Base {
  override val typeResolver: Future[ActorRef] = _

  override def reactInit(resourceToProcess: String): Unit = typeResolver onComplete {
    case Success(ref) => ref ! resourceToProcess
    case Failure(ex) => log.error(ex.getMessage)
  }

  override def typeSolved(tp: ru.egorodov.server.actors.instance.Type.Type) = {
  }
}
