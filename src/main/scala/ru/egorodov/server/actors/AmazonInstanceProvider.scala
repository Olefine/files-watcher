package ru.egorodov.server.actors

import akka.actor.{Actor, ActorLogging}
import awscala._
import ru.egorodov.server.files.FileInfo
import ru.egorodov.server.services.AmazonInstance

class AmazonInstanceProvider extends Actor with ActorLogging {
  override def receive: Receive = {
    case actions.Amazon.EC2.CreateInstance(resourceLink) =>
      val fi = FileInfo(resourceLink)
      sender ! new AmazonInstance(fi).getInstance
  }
}
