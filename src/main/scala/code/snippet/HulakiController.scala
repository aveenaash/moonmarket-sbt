package code.snippet

import scala.concurrent.{ future, promise }
import scala.concurrent.ExecutionContext.Implicits.global
import dispatch._
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
import org.elasticsearch.client.transport.TransportClient
import java.util
import net.liftweb.actor.LAFuture

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
	    
      val future_ : LAFuture[String] = new LAFuture()

      val urlRequest = url(request)
      // val http = new Http with thread.Safety
      val future = Http(urlRequest OK as.String)

      var resp = "";
      //for (f <- future) {
      //  resp = f
      //}

      future onSuccess {
	      case json =>
		resp = json
		logger.info(s"[onSuccess] response => ${json}")
                SetHtml("response", Text(resp+"")) //FIXME
		logger.info(s"[onSuccess] response => ${json}")

      }

      future onFailure {
	      case exception => 
	        logger.info(s"${exception.getMessage()}")
      }
      Thread.sleep(2000)
      logger.info("[outside] response => ${resp}")
      SetHtml("response", Text(resp))
    }


    "@requestid"  #> text(requestid, s => requestid = s) &
    "@request"    #> text(request  , s => request = s) &
    "@response"   #> textarea(responseString, s=>responseString = s) &
    "@submit"     #> ajaxSubmit("Save", process) &
    "@processreq" #> ajaxSubmit("Send Request", processRequest)

  }//end of render

}
