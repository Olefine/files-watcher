package worker

import akka.actor.{Actor, ActorLogging}
import akka.cluster.Cluster
import akka.cluster.ClusterEvent._
import akka.event.LoggingReceive

import message_bus._

class Worker(val resourceLink: String) extends Actor with ActorLogging {
  private case object SelfTerminate
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

    case Messages.Job.Start(classToEval) =>
      try {
        val eval = utils.ClassEvaluator(classToEval)
        log.info(s"\n\n Evaluating:\n $classToEval")
        log.info(s"With parameter: $resourceLink")

        sender ! Messages.Job.Finished(eval(resourceLink))
        self ! SelfTerminate
      } catch {
        case ex: Throwable => sender ! ex
      }

    case SelfTerminate =>
      cluster.leave(cluster.selfAddress)
      context.system.terminate()
  }
}
