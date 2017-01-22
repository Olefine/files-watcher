package ru.egorodov.server.actors.deploy

import akka.actor.Actor.Receive
import akka.actor.{Actor, ActorLogging, ActorRef}

import scala.concurrent.Future

class Remote extends Base {
  override val typeResolver: Future[ActorRef] = _
  def reactInit: Unit = _
}
