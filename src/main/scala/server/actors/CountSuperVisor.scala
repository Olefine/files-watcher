package server.actors

import akka.actor.{Actor, Props}
import scala.concurrent.Future
import akka.pattern.ask

case object Start

class CountSuperVisor(filename: String) extends Actor with server.implicits.Timeouts {
  val wordsCountActor = context.system.actorOf(Props[WordCalculator])
  val persistentActor = context.system.actorOf(Props[PersistentActor])

  def receive = {
    case Start =>
      val lines = io.Source.fromFile(filename).getLines.toList
      val countF: Future[Map[String, Int]] = (wordsCountActor ? Count(lines)).mapTo[Map[String, Int]]

      persistentActor ! server.actors.Create(countF)
      sender ! countF
  }
}
