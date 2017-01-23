package message_bus

case object Messages {
  case object isReady
  case object Ready

  case object Job {
    case class Start(classString: String)
    case object Suspend
    case object Terminate
    case object End
  }
}
