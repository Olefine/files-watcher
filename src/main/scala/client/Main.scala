package client

import scala.scalajs.js.JSApp
import org.scalajs.jquery.{JQueryAjaxSettings, JQueryXHR, jQuery}

import scala.scalajs.js
import org.scalajs.jquery._

case class Entry(field: String, value: Int)

object Main extends JSApp {
  def main(): Unit = {
    jQuery("body").find(".run-job").click { d: JQueryEventObject =>
      val rawFileId = d.delegateTarget.parentNode.attributes.getNamedItem("id").value
      val fileId = rawFileId.replace("file_", "")
      val requestUrl = s"/files/$fileId"

      jQuery.ajax(js.Dynamic.literal(
        url = requestUrl,
        success = { (data: js.Any, textStatus: js.JSStringOps, jqXHR: JQueryXHR) =>
          val results = js.JSON.parse(jqXHR.responseText).asInstanceOf[js.Dictionary[Int]]
          val entries = (for((prop, value) <- results) yield Entry(prop, value)).toList

          val wordCountsTable = new client.views.WordCountsView(scalatags.Text).wordsTable(entries)

          jQuery("body").find(s"[id='$rawFileId']").append(wordCountsTable)
        },
        error = { (jqXHR: JQueryXHR, textStatus: js.JSStringOps, errorThrow: js.JSStringOps) =>
          println(s"jqXHR=$jqXHR,text=$textStatus,err=$errorThrow")
        },
        `type` = "GET"
      ).asInstanceOf[JQueryAjaxSettings])
    }
  }
}