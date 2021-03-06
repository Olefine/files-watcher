package ru.egorodov.server.actors

import akka.actor.{Actor, ActorLogging, ActorPath, ActorRef, Props}

import scala.concurrent.Future
import ru.egorodov.server.utils.WorkModelSettings

import message_bus._

class JobSuperVisor extends Actor with ru.egorodov.server.implicits.Timeouts with ActorLogging {
//  val wordsCountActor = context.system.actorOf(Props[WordCalculator])
//  val persistentActor = context.system.actorOf(Props[PersistentActor])
//  val instanceProvider = context.system.actorOf(Props[AmazonInstanceProvider])
//  val deployWorker = context.system.actorOf(Props[AmazonDeployWorkerActor])

  val stages: Seq[ActorRef] = Seq()

  def receive = {
    case Messages.Job.Finished(result) =>
      log.info("Job is finished")
      log.info(s"Result: $result")
    case actions.Job.Start2(worker) => worker ! Messages.Job.Start(getJobFileContent)
    case actions.Job.Entry(file) =>
      log.info("Resolving worker strategy...")
      import concurrent.ExecutionContext.Implicits.global

      getWorkerStrategy onComplete {
        case scala.util.Success(modeActor) => modeActor ! actions.InitDeploy(file)
        case scala.util.Failure(ex) => log.error(ex.getMessage)
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
    if (WorkModelSettings.isStandalone) context.actorSelection("/user/root/standalone").resolveOne()
    else context.system.actorSelection("/user/root/remote").resolveOne()

  private def getJobFileContent: String = {
    scala.io.Source.fromFile("/Users/egorgorodov/dev/scala/files-watcher/src/main/scala/ru/egorodov/server/jobs/WordsCounts.scala.example").mkString("")
  }
}
