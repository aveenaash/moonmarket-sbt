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

/**
  * @author : prayagupd
  */

object RestController extends RestHelper {
    
     serve {
        case JsonGet("api" :: "moonmarket" :: _, _) => 
	        val builder = new ZazzercodeRequestBuilder
		JString(builder.executeQuery("Prayag"))

	case JsonGet("api" :: "tweets" :: _, _) =>
		val service = new ZazzercodeService
		service.tweets()
                //JsonResponse(
	        //  ("tweets" -> service.tweets()) 
	        //)

	case JsonGet("api" :: "chat" ::  _, _) =>
		val service = new ZazzercodeService
                val documents_ = Map("customerId" -> "1", 
		       "firstName" -> "Prayag1", 
		       "lastName" -> "Upd")
		JString(service.insertDocuments(documents_)+"")
     }

}
