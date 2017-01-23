package ru.egorodov.server.actors

import akka.actor.{Actor, ActorLogging, ActorRef, Props, RootActorPath}
import akka.cluster.{Cluster, Member}
import akka.cluster.ClusterEvent._
import akka.event.LoggingReceive
import akka.util.Timeout
import akka.pattern.ask

import scala.concurrent.{Await, Future}
import message_bus._

import scala.util.{Failure, Success}

class EntryPoint extends Actor with ActorLogging {
  private case object ReadyToPublishJob

  private val jobSuperVisor = context.system.actorOf(Props[JobSuperVisor])
  private val workers = collection.mutable.ArrayBuffer[ActorRef]()
  val cluster = Cluster(context.system)

  override def preStart() {
    initializeDeployStage
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
        self ! ReadyToPublishJob
      }
      log.info(s"[Listener] node is up: $member")

    case UnreachableMember(member) =>
      log.info(s"[Listener] node is unreachable: $member")

    case MemberRemoved(member, prevStatus) =>
      log.info(s"[Listener] node is removed: $member after $prevStatus")

    case ev: MemberEvent =>
      log.info(s"[Listener] event: $ev")

    case jobRequest: actions.JobRequest => jobSuperVisor ! actions.Job.Entry(jobRequest.resourceLink)

    case ReadyToPublishJob =>
      //TODO write wrapper for list of workers to deal with it as if one primitive
      log.info("Ready to Publish Job")
      implicit val timeout = akka.util.Timeout(10, concurrent.duration.SECONDS)
      //TODO think about protobuf serializer
      val workerStatuses = workers map { w =>
        w ? Messages.isReady
      }

      import scala.concurrent.ExecutionContext.Implicits.global
      workerStatuses foreach {
        case response: Future[Any] => response onComplete {
          case Success((worker, Messages.Ready)) =>
            log.info("Ready to start job")
            jobSuperVisor ! worker

          case Failure(ex) => log.error(ex.getMessage)
        }
      }
  }

  private def registerNewWorker(member: Member): Unit = {
    import concurrent.duration._
    implicit val resolveTimeout = Timeout(5 seconds)

    val memberRef = Await.result(context.actorSelection(RootActorPath(member.address) / "user" / "worker").resolveOne(), resolveTimeout.duration)

    workers.append(memberRef)
  }

  private def initializeDeployStage: Unit = {
    log.info("starting strategy actors(Standalone, Remote)")
//    context.actorOf(Props[deploy.Remote])
    context.actorOf(Props[deploy.Standalone], "standalone")
  }
}
