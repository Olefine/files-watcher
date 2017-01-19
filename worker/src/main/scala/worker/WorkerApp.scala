package worker

import java.io.BufferedReader

import akka.actor.{ActorSystem, Props}
import com.typesafe.config.ConfigFactory

object WorkerApp extends App {
  val system = ActorSystem("files-watcher")
  system.actorOf(Props[Worker], "worker")

  system.awaitTermination()
}
