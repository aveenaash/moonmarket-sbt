package code.snippet


import net.liftweb.util.Helpers._
import net.liftweb.http.SHtml
import net.liftweb.http.js.{JsCmd, JE}
import net.liftweb.common.Loggable
import net.liftweb.json.JsonAST._
import net.liftweb.http.js.JsCmds.Alert
import net.liftweb.json.DefaultFormats

/**
 * http://books.google.com.np/books?id=llLx053H0BsC&pg=PA99&lpg=PA99&dq=SHtml.jsonCall+liftweb&source=bl&ots=lf36X3PF_f&sig=HKca2cwVDR9JMT_PJBb0JOOMrfw&hl=en&sa=X&ei=-qpkU5TtIYL58QXkzoLYBQ&redir_esc=y#v=onepage&q=SHtml.jsonCall%20liftweb&f=false
 */

object MarketController extends Loggable {
	implicit val formats = DefaultFormats

	case class Market(rate1 : Int, rate2 : Int, standard : Int){
		def valid_? = rate1 + rate2 == standard
	}

	def render = {
		def validate (value : JValue ) : JsCmd = {
			value.extractOpt[Market].map(_.valid_?) match {
				case Some(true)  => Alert("You can purchase it.")
				case Some(false) => Alert("Sorry, add more price.")
				case None        => Alert("WTF")
			}
		}

 	        "button [onclick]" #> SHtml.jsonCall( JE.Call("land"), validate _ )
	}
}
