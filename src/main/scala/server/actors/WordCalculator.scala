package server.actors

import akka.actor.Actor

case class Count(contentToCount: List[String])

class WordCalculator extends Actor {
  def receive = {
    case Count(contentToCount) =>
      sender ! contentToCount.flatMap(line => line.split(" ")).map(word => (word, 1))
        .groupBy(_._1)
        .filter(_._1 != "")
        .map { case (_, traversable) => traversable.reduce{
          (a,b) => (a._1, a._2 + b._2)}
        }
        .filterNot(_._1.startsWith("*"))
        .filterNot(_._1.contains("."))
  }

}
