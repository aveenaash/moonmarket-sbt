package code.snippet

import net.liftweb.util.Helpers._
import net.liftweb.http.SHtml.{text,textarea,ajaxSubmit}
import net.liftweb.http.SHtml
import net.liftweb.http.js.JsCmd
import net.liftweb.http.js.JsCmds.SetHtml
import net.liftweb.common.Loggable
import xml.Text
import net.liftweb.http.S
import net.liftweb.common._
import zazzercode.ElasticsearchManager
import dispatch._, Defaults._
import org.elasticsearch.client.transport.TransportClient
import java.util

//import code.model.Customer


class HulakiController extends Loggable {


  //This is for ajax request processing
  /*def render = {

    var name = ""
    var request = ""

    def process() : JsCmd = SetHtml("response", Text(request))

    "@requestid" #> text(name, s => name = s) &
      "@request" #> text(request, s => request = s) &
      "type=submit" #> ajaxSubmit("Save to Elasticsearch", process)
      }

  }*/


  def render = {
    var requestid = ""
    var request = ""
    var responseString = ""

    def process() : JsCmd = {
      S.notice("Hello " + requestid + "------"+ request)
      //val response = new Response("1", responseString)
      //var hulaki = new Hulaki(requestid, request, response)

      val requestdoc = Map("requestId" -> requestid,
        "request" -> request)
      val responsedoc=Map("response" -> responseString,
        "responseId" -> "1")


      ElasticsearchManager.insertResponseRecord(requestdoc,responsedoc,"gccount","Api")
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
    }
    //      SetHtml("response", Text(request))


    "@requestid" #> text(requestid, s => requestid = s) &
      "@request" #> text(request, s => request = s) &
      "@response" #> textarea(responseString, s=>responseString = s) &
      "@submit" #> ajaxSubmit("Go",process) &
      "@processreq" #> ajaxSubmit("Process Request", processRequest)




  }

  //This is for normal plain form processing
  //var requestid = ""
//  def render = {
//    var requestid = ""
//    var request = ""
//    var responseString = ""
//
//    def process() ={
//      S.notice("Hello " + requestid + "------"+ request)
//      //val response = new Response("1", responseString)
//      //var hulaki = new Hulaki(requestid, request, response)
//
//      val requestdoc = Map("requestId" -> requestid,
//        "request" -> request)
//      val responsedoc=Map("response" -> responseString,
//        "responseId" -> "1")
//
////      var  map : util.Map[String, String]= new util.HashMap[String, String]()
////      map.put("requestId", requestid)
////      map.put("request",request)
//
//      //println("Response : "+hulaki.response.response)
//      ElasticsearchManager.insertResponseRecord(requestdoc,responsedoc,"gccount","Api")
//    }
//
//    def processAjax() : JsCmd = println(Text(request))
////      SetHtml("response", Text(request))
//
//
//    "@requestid" #> text(requestid, s => requestid = s) &
//      "@request" #> text(request, s => request = s) &
//      "@response" #> textarea(responseString, s=>responseString = s) &
//      "@submit" #> SHtml.onSubmitUnit(process) &
//      "@processreq" #> ajaxSubmit("Process Request", processAjax)
//
//
//
//
//  }




}
