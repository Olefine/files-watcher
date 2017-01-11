package server.persistence

import org.mongodb.scala.{Completed, Observable, Observer}
import org.mongodb.scala.bson.collection.immutable.Document

import concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global
import scala.util.Success

case object WordsCountRepository extends PersistenceBase {
  private val collection = Database.database.getCollection("counts")

  def create(entry: Future[Map[String, Int]]) = {
    entry onComplete {
      case Success(result) => {
        val doc: Document = Document(result.asInstanceOf[Map[String, Int]])
        val observable: Observable[Completed] = collection.insertOne(doc)

        observable.subscribe(new Observer[Completed] {
          override def onError(e: Throwable): Unit = println(e.getMessage)

          override def onComplete(): Unit = println("completed")

          override def onNext(result: Completed): Unit = println("inserted")
        })
      }
    }
  }
}
