package ru.egorodov.server.actors.deploy

import akka.actor.{Actor, ActorLogging, ActorRef}
import ru.egorodov.server.actors.actions.{InitDeploy, TypeSolved}
import ru.egorodov.server.utils.DeploySettings

trait Base extends Actor with ActorLogging {
  val typeResolver: ActorRef

  override def preStart(): Unit = {
    super.preStart()
    log.info(s"Starting ${self.getClass} on address = ${self.path.toString}")
  }

  override def receive = {
    case InitDeploy(resourceToProcess) => reactInit(resourceToProcess)
    case TypeSolved(tp, resource) => typeSolved(tp, resource)
  }

  def reactInit(resourceToProcess: String)

  def typeSolved(tp: ru.egorodov.server.actors.instance.Type.Type, resource: String) = {
    log.info(s"Initiate worker deploy with type: $tp")

    deploy(resource)
  }

  def deploy(resource: String): Unit = {
    import sys.process._

    val workerPath = DeploySettings.workerPath
    val proc = Process(s"(java -jar $workerPath $resource) &").!

    proc match {
      case 0 => log.info("Worker process was successfully started.")
      case _ => log.error("Something goes wrong.")
    }
  }
}
