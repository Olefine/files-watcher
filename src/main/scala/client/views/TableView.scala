package client.views

import client.HasViewOf

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
