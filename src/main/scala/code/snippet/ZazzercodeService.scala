package zazzercode

import net.liftweb._
import common._
import http._
import rest._
import net.liftweb.json.JsonDSL._
import net.liftweb.json.JsonParser._
import net.liftweb.json.JsonAST._
import net.liftweb.http.js._
import net.liftweb.http.js.JE.JsObj
import net.liftweb.http.js.JE.JsArray

class ZazzercodeService {

     def tweets() = {

             val jsObject = JsObj(("tweets", JsArray(List(
		                                     JsObj(("2012", "VIm is great"), ("2013", "Prayag Upd is great")), 
	                                             JsObj(("2011", "So called friend"), ("2012", "Lazarus"))
						    )
					    )
				    )
			    )
	     val response = jsObject.toJsCmd.toString.getBytes("UTF-8")
	     //Response(response, List("Content-Length" -> response.length.toString, "Content-Type" -> "application/json"), Nil, 200)
             JsonResponse(jsObject)
     }

/*
     def tweets_() = {

	     Response (
		        ("prayagupd" -> "When everything is dim, use VIm") 
	                ~ 
	                ("porcupinetree" -> "Prayag Upd has joined PT from today.")
		)
             
     }
*/

    def insertDocuments(documents : Map[String, String]) = {
        ElasticsearchManager.insertDocuments(documents, Constants.EsIndex, Constants.EsTypeCustomer)
    }
}
