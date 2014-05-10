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
  private var messages: List[String] = Nil

  /**
   * When the component is instantiated, register as
   * a listener with the ChatServer
   */
  def registerWith = HulakiListenerManager

  private def sendMessage(msg: String) = 
	  HulakiListenerManager ! msg

  /**
   * The CometActor is an Actor, so it processes messages.
   * In this case, we're listening for Vector[String],
   * and when we get one, update our private state
   * and reRender() the component.  reRender() will
   * cause changes to be sent to the browser.
   */
  override def lowPriority = {
    case msg: List[String] => { 
	    messages = msg; 
	    reRender(false)
    }
  }

  private def renderMessages = <div>{messages.take(10).reverse.map(m => <li>{m}</li>)}</div>

  /**
   * Put the messages in the li elements and clear
   * any elements that have the clearable class.
   */
  def render = bind("json", "input"    -> ajaxForm(SHtml.text("", sendMessage _)),
                            "response" -> renderMessages)


}
