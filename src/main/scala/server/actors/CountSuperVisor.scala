package server.actors

import akka.actor.{Actor, ActorLogging, Props}

import scala.concurrent.Future
import akka.pattern.ask
import awscala.ec2.EC2
//import com.amazonaws.services.ec2.model.InstanceType

import scala.concurrent.ExecutionContext.Implicits.global

case object Start
case object RunJob

class CountSuperVisor(filename: String) extends Actor with server.implicits.Timeouts with ActorLogging {
  val wordsCountActor = context.system.actorOf(Props[WordCalculator])
  val persistentActor = context.system.actorOf(Props[PersistentActor])

  def receive = {
    case Start =>
      val lines = scala.io.Source.fromFile(filename).getLines.toList
      val countF: Future[Map[String, Int]] = (wordsCountActor ? Count(lines)).mapTo[Map[String, Int]]

      persistentActor ! server.actors.Create(countF)
      sender ! countF
    case "WorkerRegistered" =>
      val result = sender ? 10
      result onSuccess {
        case n: Int => log.info(s"\n\n\n\nWorker responds with number ${n}\n\n\n\n")
      }
    case RunJob => {
//      import awscala._
//
//      implicit val ec2 = EC2.at(Region.Singapore)
//
//      import scala.concurrent._
//      import scala.concurrent.ExecutionContext.global

      //val newInstance = Future("ami-64be1507", ec2.keyPairs.head, instanceType = InstanceType.R3Large)
    }
    case s: String => log.error(s)

  }
}
