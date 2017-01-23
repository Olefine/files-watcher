package ru.egorodov.server.actors
import akka.actor.ActorRef

import scala.util.Try

object actions {
  case class JobRequest(resourceLink: String)

  case class InitDeploy(resourceToProcess: String)
  case class TypeSolved(tp: ru.egorodov.server.actors.instance.Type.Type, resource: String)

  case object Job {
    case class Start(file: String)
    case class Start2(worker: ActorRef)
    case class Entry(file: String)
  }

  case object Amazon {
    case object EC2 {
      case class CreateInstance(resourceLink: String)
      case object Terminate
    }

    case object Deploy {
      case class Instance(instances: Seq[awscala.ec2.Instance])
      case class Result(result: Try[String])
    }
  }
}
