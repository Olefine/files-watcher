package ru.egorodov.server.services

import java.io.File

import awscala._
import awscala.ec2.EC2
import com.amazonaws.services.ec2.model.InstanceType
import ru.egorodov.server.files.FileInfo

import scala.concurrent.Future

class AmazonInstance(val resourceInfo: FileInfo) {
  def instanceType: InstanceType = {
    val sizeBytes: Double = resourceInfo.size
    val sizeGb = sizeBytes / 1024 / 1024 / 1024

    sizeGb match {
      case size if (0 to 12).contains(size.toInt)=> InstanceType.R3Large
      case size if (12 to 25).contains(size.toInt) => InstanceType.R3Xlarge
      case size if (25 to 52).contains(size.toInt) => InstanceType.R32xlarge
      case size if (52 to 115).contains(size.toInt) => InstanceType.R34xlarge
      case _ => InstanceType.R38xlarge
    }
  }

  def getInstance = {
    import scala.concurrent.ExecutionContext.Implicits.global

    implicit val ec2 = EC2.at(Region.Singapore)
    val listOfInstances = Future(ec2.runAndAwait("ami-4dd6782e", ec2.keyPairs.head, instanceType = instanceType))

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
