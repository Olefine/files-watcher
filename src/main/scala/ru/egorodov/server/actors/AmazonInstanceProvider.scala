package ru.egorodov.server.actors

import akka.actor.{Actor, ActorLogging}
import awscala._
import ec2._
import awscala.ec2.EC2
import com.amazonaws.services.ec2.model.InstanceType
import ru.egorodov.server.files.FileInfo
import ru.egorodov.server.services.AmazonInstance

import scala.concurrent.Future
class AmazonInstanceProvider extends Actor with ActorLogging {
  override def receive: Receive = {
    case actions.Amazon.EC2.CreateInstance(resourceLink) =>
      val fi = FileInfo(resourceLink)
      sender ! new AmazonInstance(fi).getInstance
  }
}
