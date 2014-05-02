package code.snippet

import net.liftweb.util.Helpers._
import net.liftweb.util.CssSel
import net.liftweb.http.SHtml.{text,textarea,ajaxSubmit}
import net.liftweb.http.SHtml
import net.liftweb.http.js.JsCmds.SetHtml
import net.liftweb.http.js.JsCmds.Run
import net.liftweb.http.js.JsCmds.Script
import net.liftweb.http.js.JsCmd
import net.liftweb.http.js.JE.{JsFunc, JsRaw}
import net.liftweb.common.Loggable
import xml.Text
import net.liftweb.http.S
import net.liftweb.common._
import zazzercode.ElasticsearchManager
import dispatch._, Defaults._
import org.elasticsearch.client.transport.TransportClient
import java.util


class HulakiController extends Loggable {


  def render = {
    var requestid = ""
    var request = ""
    var responseString = ""

    def process() : JsCmd = {
      S.notice("Hulaki " + requestid + "------"+ request)

      val requestdoc  = Map("requestId" -> requestid,
                            "request"   -> request)
      val responsedoc = Map("response"   -> responseString,
                            "responseId" -> "1")

      ElasticsearchManager.insertResponseRecord(requestdoc, responsedoc, "gccount", "Api")

      SetHtml("response", Text("Response is saved...."))
    }

    def processRequest() : JsCmd = {
      val svc = url(request)
      //val svc = url("http://localhost:8082/api/tweets")
      val country = Http(svc OK as.String)

      var resp = "";
      for (c <- country) {
        resp = c
      }
      Thread.sleep(1000)

      SetHtml("response", Text(resp+""))
      // Run("$('#response').html('resp')")
      // val command_ = JsFunc("format").cmd
      // "*" #> Script(command_)
    }


    "@requestid"  #> text(requestid, s => requestid = s) &
    "@request"    #> text(request  , s => request = s) &
    "@response"   #> textarea(responseString, s=>responseString = s) &
    "@submit"     #> ajaxSubmit("Save", process) &
    "@processreq" #> ajaxSubmit("Send Request", processRequest)

  }//end of render

}
