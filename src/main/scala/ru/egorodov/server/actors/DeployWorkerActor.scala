package ru.egorodov.server.actors

import akka.actor.{Actor, ActorLogging}
import com.decodified.scalassh.Validated

import scala.util.{Failure, Success, Try}

class DeployWorkerActor extends Actor with ActorLogging {
  override def receive = {
    case actions.Amazon.Deploy.Instance(instancesSeq) =>
      instancesSeq.find { inst =>
        val instanceType = inst.state.getName
        instanceType == "running"
      } match {
        case Some(instance) => {
          log.info(s"Start Uploading worker to ${instance.instanceId}")

          val uploadResult: Validated[Unit] = instance.withKeyPair(new java.io.File("egorodov-personal.pem"),"ubuntu") { i =>
            i.ssh { ssh =>
              ssh.upload(new java.io.File("worker/builds/worker-assembly-0.0.1-Worker.jar").getAbsolutePath, "worker-assembly-0.0.1-Worker.jar")
            }
          }

          sender ! actions.Amazon.Deploy.Result(handleUploadResult(uploadResult))
        }
        case None => log.error("Can not find instance to deploy Worker")
      }
  }

  private def handleUploadResult(validated: Validated[Unit]): Try[String] = {
    validated match {
      case Left(message) => Failure(new RuntimeException(message))
      //TODO implement
      case Right(()) => Success("here the instance_Id")
    }
  }
}
