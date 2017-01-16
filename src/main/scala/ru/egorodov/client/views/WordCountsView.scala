package client.views
import client.Entry

class WordCountsView[Builder, Output <: FragT, FragT](val bundle: scalatags.generic.Bundle[Builder, Output, FragT]) {
  def wordsTable(ents: List[Entry]) = {
    import bundle.all._

    table(
      `class`:= "table",
      thead(
        tr(
          th("Word"),
          th("Count")
        )
      ),
      tbody(
        ents.map {entry =>
          tr(
            td(entry.field),
            td(entry.value)
          )
        } : _*
      )
    ).render
  }
}
