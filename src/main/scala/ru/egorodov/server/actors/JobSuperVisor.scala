package ru.egorodov.server.actors

import akka.actor.{Actor, ActorLogging, ActorRef, Props}

import scala.concurrent.Future
import akka.pattern.ask
import ru.egorodov.server.utils.WorkModelSettings

class JobSuperVisor extends Actor with ru.egorodov.server.implicits.Timeouts with ActorLogging {
  val wordsCountActor = context.system.actorOf(Props[WordCalculator])
  val persistentActor = context.system.actorOf(Props[PersistentActor])
  val instanceProvider = context.system.actorOf(Props[AmazonInstanceProvider])
  val deployWorker = context.system.actorOf(Props[AmazonDeployWorkerActor])

  val stages: Seq[ActorRef] = Seq()

  private var _resourceLink: Option[String] = None


  def receive = {
    case actions.Job.Start(file) =>
//      val lines = scala.io.Source.fromFile(file).getLines.toList
//      val countF: Future[Map[String, Int]] = (wordsCountActor ? Count(lines)).mapTo[Map[String, Int]]
//
//      persistentActor ! Create(countF)
//      sender ! countF
    case actions.Job.Entry(file) =>
      _resourceLink = Some(file)
      getWorkerStrategy onSuccess {
        case modeActor: ActorRef => modeActor ! actions.InitDeploy
      }
//      import scala.concurrent.ExecutionContext.Implicits.global
//      val instanceRequest = instanceProvider ? actions.Amazon.EC2.CreateInstance(file)
//      instanceRequest onSuccess {
//        case request: Future[Any] =>
//          request onSuccess {
//            case result => deployWorker ! actions.Amazon.Deploy.Instance(result.asInstanceOf[Seq[awscala.ec2.Instance]])
//          }
//
//          request onFailure {
//            case r => println(r.getMessage)
//          }
//      }

    case actions.Amazon.Deploy.Result(rs) =>
      rs match {
        case scala.util.Success(instanceId) => println(instanceId)
        case scala.util.Failure(ex) => throw ex
      }
      log.info("Deploy Finished")
    case s: String => log.error(s)
  }

  private def getWorkerStrategy: Future[ActorRef] =
    if (WorkModelSettings.isStandalone) context.system.actorSelection("/user/standalone").resolveOne()
    else context.system.actorSelection("/user/remote").resolveOne()
}
