package client

import scala.scalajs.js.JSApp
import org.scalajs.jquery.{JQueryAjaxSettings, JQueryXHR, jQuery}

import scala.scalajs.js
import org.scalajs.jquery._

case class Entry(field: String, value: Int)

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
    )
  }
}
object Main extends JSApp {

  def main(): Unit = {
    val rootBlock = jQuery("#root-block")

    jQuery("body").find(".run-job").click { d: JQueryEventObject =>
      val rawFileId = d.delegateTarget.parentNode.attributes.getNamedItem("id").value
      val fileId = rawFileId.replace("file_", "")
      val requestUrl = s"/files/${fileId}"

      jQuery.ajax(js.Dynamic.literal(
        url = requestUrl,
        success = { (data: js.Any, textStatus: js.JSStringOps, jqXHR: JQueryXHR) =>
          val results = js.JSON.parse(jqXHR.responseText).asInstanceOf[js.Dictionary[Int]]
          val entries = (for((prop, value) <- results) yield Entry(prop, value)).toList

          val textExample = new WordCountsView(scalatags.Text).wordsTable(entries).render

          jQuery("body").find(s"[id='${rawFileId}']").append(textExample)
        },
        error = { (jqXHR: JQueryXHR, textStatus: js.JSStringOps, errorThrow: js.JSStringOps) =>
          println(s"jqXHR=$jqXHR,text=$textStatus,err=$errorThrow")
        },
        `type` = "GET"
      ).asInstanceOf[JQueryAjaxSettings])
    }
  }
}