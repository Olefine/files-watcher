package server.actors

import akka.actor.{Actor, ActorLogging}
import awscala._, ec2._
import awscala.ec2.EC2
import com.amazonaws.services.ec2.model.InstanceType
import server.files.FileInfo

import scala.concurrent.Future
class AmazonInstanceProvider extends Actor with ActorLogging {
  override def receive: Receive = {
    case actions.Amazon.EC2.Create(resourceLink) => getInstance(resourceLink)
  }

  private def instanceTypeResolver(resourceLink: String): InstanceType = {
    val sizeBytes: Float = FileInfo(resourceLink).size
    val sizeGb: Float = sizeBytes / 1024 / 1024 / 1024

    sizeBytes match {
      case _ => InstanceType.T2Micro
      case size if 0 to 12 contains size => InstanceType.R3Large
      case size if 12 to 25 contains size => InstanceType.R3Xlarge
      case size if 25 to 52 contains size => InstanceType.R32xlarge
      case size if 52 to 115 contains size => InstanceType.R34xlarge
    }
  }

  private def getInstance(fileLink: String) = {
    import scala.concurrent.ExecutionContext.Implicits.global

    implicit val ec2 = EC2.at(Region.Singapore)
    val listOfInstances = Future(ec2.runAndAwait("ami-4dd6782e", ec2.keyPairs.head, instanceType = instanceTypeResolver(fileLink)))

    for {
      instances <- listOfInstances
      instance <- instances
    } {
      instance.withKeyPair(new File("egorodov-personal.pem")) { inst =>
        inst.ssh { ssh =>
          ssh.upload(new java.io.File("README.md").getAbsolutePath, "README.md")
        }
      }
    }
  }
}
