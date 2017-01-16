package ru.egorodov.server.actors

import akka.actor.{Actor, ActorLogging, RootActorPath}
import akka.cluster.Cluster
import akka.cluster.ClusterEvent._
import akka.event.LoggingReceive

class EntryPoint extends Actor with ActorLogging {
  val cluster = Cluster(context.system)

  override def preStart() {
    cluster.subscribe(self, InitialStateAsEvents, classOf[MemberEvent],
      classOf[UnreachableMember])
  }

  override def postStop() {
    cluster.unsubscribe(self)
  }

  def receive = LoggingReceive {
    case MemberUp(member) =>
      if (member.hasRole("worker"))
        context.actorSelection(RootActorPath(member.address) / "user" / "worker") ! "hello"
      log.info(s"[Listener] node is up: $member")

    case UnreachableMember(member) =>
      log.info(s"[Listener] node is unreachable: $member")

    case MemberRemoved(member, prevStatus) =>
      log.info(s"[Listener] node is removed: $member after $prevStatus")

    case ev: MemberEvent =>
      log.info(s"[Listener] event: $ev")
  }
}
