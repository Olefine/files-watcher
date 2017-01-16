package ru.egorodov.server.actors

object actions {
  case object Counts {
    case class Start(file: String)
    case class Start2(file: String)
  }

  case object Amazon {
    case object EC2 {
      case class Create(resourceLink: String)
      case object Terminate
    }
  }
}
