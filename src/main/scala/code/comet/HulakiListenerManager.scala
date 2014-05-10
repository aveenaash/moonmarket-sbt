package code
package comet

import dispatch._
import scala.concurrent.{ future, promise }
import scala.concurrent.ExecutionContext.Implicits.global

import net.liftweb._
import http._
import actor._
import net.liftmodules.amqp.AMQPMessage
import code.comet.Rabbit.RemoteSender
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
  var messages: List[String] = Nil

  /**
   * When we update the listeners, what message do we send?
   * We send the msgs, which is an immutable data structure,
   * so it can be shared with lots of threads without any
   * danger or locking.
   */
  def createUpdate = messages

  /**
   * process messages that are sent to the Actor.  In
   * this case, we're looking for Strings that are sent
   * to the ChatServer.  We append them to our Vector of
   * messages, and then update all the listeners.
   */
  override def lowPriority = {
    case msg: String => {
	    println("message => "+msg)
	    val request = msg
	    val urlRequest = url(request)
            val future = Http(urlRequest OK as.String)

            future onSuccess {
	      case json =>
		println(s"[onSuccess] response => ${msg}")
                messages ::= json
		println(s"[onSuccess] response => ${json}")
                updateListeners()

            }

            future onFailure {
	      case exception => 
	        messages ::= exception.getMessage()
	        println(s"${exception.getMessage()}")
		updateListeners()
            }
    }
  }//end of lowPriority
}
