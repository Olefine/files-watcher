package client

import scalatags._

trait HasViewOf[A <: String] {
  //TODO fixme using typeclasses
  def wrap: Text.TypedTag[String]
  def view: String
}


class TableView(val cols: Seq[String], implicit val data: List[Seq[String]]) extends HasViewOf[String] {

  def view = {
    wrap.render
  }

  def wrap = {
    import scalatags.Text.all._
    div()

    table(
      `class`:= "table",
      thead(
        tr(
          this.cols.map { col =>
            th(col)
          } : _*
        )
      ),
      tbody(
        this.data.map {row =>
          tr(
            row.map { rowItem =>
              td(rowItem)
            }
          )
        }
      )
    )
  }
}

//trait View
//
//case class ElemView(value: String) extends HasViewOf[String]{
//  import scalatags.Text.all._
//
//  override def wrap = {
//    p(value)
//  }
//}
//
//class ListView(eachTag: => scalatags.Text.TypedTag[String], val elems: Seq[HasViewOf[String]]) extends HasViewOf[String] {
//  def wrap = {
//    elems.map { elem =>
//      eachTag(elem.wrap): _*
//    } : _*
//  }
//}
//
//object R {
//  import scalatags.Text.all._
//  val res = new ListView(div, Seq(ElemView("egor")))
//  new ListView(div(`class` := "file", `id` := "file_id"), Seq(res))
//}
