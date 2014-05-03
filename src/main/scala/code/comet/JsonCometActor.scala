package code {
package comet {

import net.liftweb._
import http._
import SHtml._ 
import net.liftweb.common.{Box, Full}
import net.liftweb.util._
import net.liftweb.actor._
import net.liftweb.util.Helpers._
import net.liftweb.http.js.JsCmds.{SetHtml}
import net.liftweb.http.js.JE.Str
import _root_.scala.xml.Text

/**
 * @author : prayagupd
 * @see : https://www.assembla.com/wiki/show/liftweb/Comet_Support
 */

class JsonCometActor extends CometActor {
	
  override def defaultPrefix = Full("resp")
  val json = "{name : prayag}"

  def render = bind("message" -> <span id="message">json : '{json}'</span>)
		
  ActorPing.schedule(this, JsonResponse, 10000L)
		
  override def lowPriority : PartialFunction[Any,Unit] = {
    case JsonResponse => {
      partialUpdate(SetHtml("message", Text("updated json : " + "{'name' : 'upd'}")))
      ActorPing.schedule(this, JsonResponse, 10000L)
    }
  }
}

case object JsonResponse

}
}
