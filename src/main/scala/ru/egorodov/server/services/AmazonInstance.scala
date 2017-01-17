package ru.egorodov.server.services
import awscala._
import awscala.ec2.{EC2, Instance}
import com.amazonaws.services.ec2.model.InstanceType
import ru.egorodov.server.files.FileInfo
import ru.egorodov.server.utils.DeploySettings

import scala.concurrent.Future

class AmazonInstance(val resourceInfo: FileInfo) {
  def instanceType: InstanceType = {
    val sizeBytes: Double = resourceInfo.size
    val sizeGb = sizeBytes / Math.pow(1024, 3) toInt

    sizeGb match {
      case size if (0 to 12).contains(size)=> InstanceType.R3Large
      case size if (12 to 25).contains(size) => InstanceType.R3Xlarge
      case size if (25 to 52).contains(size) => InstanceType.R32xlarge
      case size if (52 to 115).contains(size) => InstanceType.R34xlarge
      case _ => InstanceType.R38xlarge
    }
  }

  def getInstance: Future[Seq[Instance]] = {
    import scala.concurrent.ExecutionContext.Implicits.global

    implicit val ec2 = EC2.at(Region.Singapore)
    Future[Seq[Instance]](ec2.runAndAwait(DeploySettings.ami, ec2.keyPairs.head, instanceType = instanceType))
  }
}
