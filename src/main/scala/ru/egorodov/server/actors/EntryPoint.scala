package ru.egorodov.server.actors

import akka.actor.{Actor, ActorLogging, ActorRef, Props, RootActorPath}
import akka.cluster.{Cluster, Member}
import akka.cluster.ClusterEvent._
import akka.event.LoggingReceive
import akka.util.Timeout

import scala.concurrent.Await

class EntryPoint extends Actor with ActorLogging {
  private val workers = collection.mutable.ArrayBuffer[ActorRef]()
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
      if (member.hasRole("worker")) {
        registerNewWorker(member)
      } else if (member.hasRole("main")) {
      }
      log.info(s"[Listener] node is up: $member")

    case UnreachableMember(member) =>
      log.info(s"[Listener] node is unreachable: $member")

    case MemberRemoved(member, prevStatus) =>
      log.info(s"[Listener] node is removed: $member after $prevStatus")

    case ev: MemberEvent =>
      log.info(s"[Listener] event: $ev")

    case jobRequest: actions.JobRequest =>
  }

  private def registerNewWorker(member: Member): Unit = {
    import concurrent.duration._
    implicit val resolveTimeout = Timeout(5 seconds)

    val memberRef = Await.result(context.actorSelection(RootActorPath(member.address) / "user" / "worker").resolveOne(), resolveTimeout.duration)

    workers.append(memberRef)
  }

  private def initializeDeployStage: Unit = {
    context.actorOf(Props[deploy.Remote])
    context.actorOf(Props[deploy.Standalone])
  }
}
