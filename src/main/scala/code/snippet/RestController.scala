package zazzercode

import net.liftweb._
import common._
import http._
import rest._
import json._

/**
  * @author : prayagupd
  */

object RestController extends RestHelper {
    
     serve {
        case JsonGet("api" :: "moonmarket" :: _, _) => 
	        val builder = new ZazzercodeRequestBuilder
		//JString(builder.executeQuery("Prayag"))
		JString(builder.execute())
     }       
}
