import akka.actor.{ActorSystem, Props}

object WorkerApp extends App {
  val system = ActorSystem("files-watcher")
  system.actorOf(Props[Worker], "worker")
  system.awaitTermination()
}
