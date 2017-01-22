package ru.egorodov.server.actors.deploy.type_resolvers

import ru.egorodov.server.actors.instance.Type
import akka.actor.Actor.Receive
import akka.actor.{Actor, ActorLogging}
import ru.egorodov.server.actors.actions

class BaseTypeResolver extends Actor with ActorLogging {
  override def receive = {
    case resourceLink: String => sender ! actions.TypeSolved(getInstanceType(resourceLink))
  }

  // hardcoded InstanceType for standalone(Full)
  // TODO rewrite to resolve instance type with exact mapping for each mode(Standalone, Cluster)
  private def getInstanceType(rl: String) = Type.Full
}
