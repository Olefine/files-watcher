package server.actors.remoting

import akka.actor.{ActorSystem, Props}
import com.typesafe.config.ConfigFactory

object Sum extends App {
  val conf = ConfigFactory.load()
  val system = ActorSystem("files-watcher", conf.getConfig("worker").withFallback(conf))
  system.actorOf(Props[Worker], "worker")
  system.awaitTermination()
}
