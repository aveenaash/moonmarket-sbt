package code
package comet

import net.liftweb._
import http._
import util._
import Helpers._
import net.liftweb.http.SHtml.{text,textarea,ajaxSubmit,ajaxForm}
import net.liftweb.http.js.JsCmds.SetHtml
import net.liftweb.http.js.JsCmds.Run
import net.liftweb.http.js.JsCmds.Script
import net.liftweb.http.js.JsCmd
import net.liftweb.http.js.JE.{JsFunc, JsRaw}
import xml.Text

import code.model.JsonMessage

/**
 * The screen real estate on the browser will be represented
 * by this component.  When the component changes on the server
 * the changes are automatically reflected in the browser.
 *
 * @see : https://www.assembla.com/wiki/show/liftweb/Comet_Support
 * couldnlt implement case class though
 */

class HulakiListener extends CometActor with CometListener {
  private var json: String = ""

  /**
   * When the component is instantiated, register as
   * a listener with the ListenerManager
   */
  def registerWith = HulakiListenerManager

  private def sendRequest(url: String) = 
	  HulakiListenerManager ! url

  /**
   * The CometActor is an Actor, so it processes json.
   * In this case, we're listening for String,
   * and when we get one, update our private state
   * and reRender() the component.  reRender() will
   * cause changes to be sent to the browser.
   */
  override def lowPriority = {
    case jsonFromManager : String => { 
	    json = jsonFromManager; 
	    reRender(false)
    }
  }

  private def renderJson = 
	  <textarea id='response' name='response' style='width: 840px; height: 450px;'>{json}</textarea>

  /**
   * Put the json to comet:textarea
   */
  def render = bind("json", "input"    -> ajaxForm(SHtml.text("", sendRequest _)),
                            "textarea" -> renderJson)


}
