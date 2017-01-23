package worker

import akka.actor.{Actor, ActorLogging}
import akka.cluster.Cluster
import akka.cluster.ClusterEvent._
import akka.event.LoggingReceive

import message_bus._

class Worker extends Actor with ActorLogging {
  val cluster = Cluster(context.system)

  override def preStart() {
    cluster.subscribe(self, InitialStateAsEvents, classOf[MemberEvent],
      classOf[UnreachableMember])
  }

  override def postStop() {
    cluster.unsubscribe(self)
  }

  def receive = LoggingReceive {
    case Messages.isReady =>
      log.info("Ready to receiving job arguments")
      sender ! (self, Messages.Ready)

    case Message.Job.Start(classToEval) =>
      try {
        utils.ClassEvaluator(classToEval)
      } catch {
        case ex => sender ! ex
      }
  }
}
