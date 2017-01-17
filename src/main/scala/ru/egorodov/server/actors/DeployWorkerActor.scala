package ru.egorodov.server.actors

import java.io.File

import akka.actor.{Actor, ActorLogging}
import com.decodified.scalassh.Validated
import ru.egorodov.server.utils.DeploySettings

import scala.util.{Failure, Success, Try}

class DeployWorkerActor extends Actor with ActorLogging {
  override def receive = {
    case actions.Amazon.Deploy.Instance(instancesSeq) =>
      instancesSeq.find { inst =>
        val instanceType = inst.state.getName
        instanceType == "running"
      } match {
        case Some(instance) => {
          val instanceId = instance.instanceId
          log.info(s"Start Uploading worker to $instanceId")

          val pem = new File(DeploySettings.keyPath)
          grantAccessLevel(pem.getAbsolutePath)

          val workerPath = new File(DeploySettings.workerPath).getAbsolutePath
          val user = DeploySettings.user

          val uploadResult: Validated[Unit] = instance.withKeyPair(pem, user) { client =>
            client.ssh(_.upload(workerPath, "worker.jar"))
          }

          sender ! actions.Amazon.Deploy.Result(handleUploadResult(instanceId, uploadResult))
        }
        case None => log.error("Can not find instance to deploy Worker")
      }
  }

  private def handleUploadResult(instanceId: String, validated: Validated[Unit]): Try[String] = {
    validated match {
      case Left(message) => Failure(new RuntimeException(message))
      case Right(()) => Success(s"Instance Id = $instanceId")
    }
  }

  private def grantAccessLevel(pemFilePath: String): Unit = sys.process.Process(s"chmod 600 $pemFilePath")
}
