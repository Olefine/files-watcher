package ru.egorodov.server.actors.deploy

import akka.actor.Actor.Receive
import akka.actor.{Actor, ActorLogging, ActorRef}
import ru.egorodov.server.actors.actions.{InitDeploy, TypeSolved}

import scala.concurrent.Future

trait Base extends Actor with ActorLogging {
  val typeResolver: Future[ActorRef]

  override def receive: Receive = {
    case InitDeploy(resourceToProcess) => reactInit(resourceToProcess)
    case TypeSolved(tp) =>
  }

  def reactInit(resourceToProcess: String)

  def typeSolved(tp: ru.egorodov.server.actors.instance.Type.Type)
}
