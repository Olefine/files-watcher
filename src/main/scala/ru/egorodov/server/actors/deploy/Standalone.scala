package ru.egorodov.server.actors.deploy

import akka.actor.Actor.Receive
import akka.actor.{Actor, ActorLogging, ActorRef, Props}

import scala.concurrent.Future
import scala.util.{Failure, Success}

class Standalone extends Base {
  override val typeResolver: ActorRef = context.system.actorOf(Props[type_resolvers.BaseTypeResolver])

  override def reactInit(resourceToProcess: String): Unit = typeResolver ! resourceToProcess
}
