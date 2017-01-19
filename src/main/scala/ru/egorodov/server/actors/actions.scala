package ru.egorodov.server.actors
import scala.util.Try

object actions {
  case object Counts {
    case class Start(file: String)
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
