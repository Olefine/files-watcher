package ru.egorodov.server.actors

import akka.actor.{Actor, ActorLogging, Props}

import scala.concurrent.Future
import akka.pattern.ask

class CountSuperVisor extends Actor with ru.egorodov.server.implicits.Timeouts with ActorLogging {
  val wordsCountActor = context.system.actorOf(Props[WordCalculator])
  val persistentActor = context.system.actorOf(Props[PersistentActor])
  val instanceProvider = context.system.actorOf(Props[AmazonInstanceProvider])

  def receive = {
    case actions.Counts.Start(file) =>
      val lines = scala.io.Source.fromFile(file).getLines.toList
      val countF: Future[Map[String, Int]] = (wordsCountActor ? Count(lines)).mapTo[Map[String, Int]]

      persistentActor ! Create(countF)
      sender ! countF
    case actions.Counts.Start2(file) =>
      instanceProvider ? actions.Amazon.EC2.Create(file)
    case s: String => log.error(s)
  }
}
