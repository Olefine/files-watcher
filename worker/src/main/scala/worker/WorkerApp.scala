package worker

import akka.actor.{ActorSystem, Props}
import com.typesafe.config.ConfigFactory

object WorkerApp extends App {
  val configFile = scala.io.Source.fromFile("worker.conf")
  val customConfig = configFile.mkString
  configFile.close()

  val conf = ConfigFactory.parseString(customConfig)
  val system = ActorSystem("files-watcher", conf)
  system.actorOf(Props[Worker], "worker")

  system.awaitTermination()
}
