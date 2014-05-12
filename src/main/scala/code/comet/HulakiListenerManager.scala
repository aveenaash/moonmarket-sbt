package code.comet

import net.liftweb.actor._
import net.liftweb.http._
import dispatch._
import scala.concurrent.{ future, promise }
import scala.concurrent.ExecutionContext.Implicits.global


object HulakiListenerManager extends LiftActor with ListenerManager
{
  private var msgs : String = ""

  def createUpdate=msgs

  override def lowPriority = {

    case clientUrl: String => {
      println("url => " + clientUrl)
      val request = clientUrl
      val urlRequest = url(request)
      val future = Http(urlRequest OK as.String)

      future onSuccess {
        case json =>
          println(s"[onSuccess] response => ${json}")
          msgs = json
          println(s"[onSuccess] response => ${msgs}")
          updateListeners()

      }

      future onFailure {
        case exception =>
          msgs = exception.getMessage()
          println(s"${exception.getMessage()}")
          updateListeners()
      }
    }
  }
}