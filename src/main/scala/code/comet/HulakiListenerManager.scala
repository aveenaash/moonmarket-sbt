package code
package comet

import dispatch._
import scala.concurrent.{ future, promise }
import scala.concurrent.ExecutionContext.Implicits.global

import net.liftweb._
import http._
import actor._
import net.liftmodules.amqp.AMQPMessage
import code.model

import zazzercode.ZazzercodeRequestBuilder

/**
 * A singleton that provides chat features to all clients.
 * It's an Actor so it's thread-safe because only one
 * message will be processed at once.
 */

object HulakiListenerManager extends LiftActor with ListenerManager {
  //TODO load previous chat messages
  //val builder = new ZazzercodeRequestBuilder()
  //private var msgs = builder.getCustomers("Prayag")
  var jsonResponse: String = ""

  /**
   * When we update the listeners, what message do we send?
   * We send the json, which is an immutable data structure,
   * so it can be shared with lots of threads without any
   * danger or locking.
   */
  def createUpdate = jsonResponse

  /**
   * process messages that are sent to the Actor.  In
   * this case, we're looking for Strings that are sent
   * to the ChatServer.  We append them to our Vector of
   * messages, and then update all the listeners.
   */
  override def lowPriority = {
    case clientUrl: String => {
	    println("url => " + clientUrl)
	    val request = clientUrl
	    val urlRequest = url(request)
            val future = Http(urlRequest OK as.String)

            future onSuccess {
	      case json =>
		println(s"[onSuccess] response => ${json}")
                jsonResponse = json
		println(s"[onSuccess] response => ${jsonResponse}")
                updateListeners()

            }

            future onFailure {
	      case exception => 
	        jsonResponse = exception.getMessage()
	        println(s"${exception.getMessage()}")
		updateListeners()
            }
    }
  }//end of lowPriority
}
