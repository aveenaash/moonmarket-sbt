package code.snippet

/*
import net.liftweb._
import net.liftweb.http._
import util._
import common._
import Helpers._
import TimeHelpers._
import net.liftweb.common.Logger
import net.liftweb.mapper._
import scala.xml._
*/

import _root_.net.liftweb._
import http._
import SHtml._
import js._
import JsCmds._
import common._
import util._
import Helpers._

import scala.xml.NodeSeq

import code.model.Customer

/*
 * this class provides the snippets that back the Customer CRUD 
 * pages (List, Create, View, Edit, Delete)
 * @author : prayagupd
 * @see    : http://simply.liftweb.net/index-4.2.html
 * @see    : https://github.com/dph01/lift-CRUDBasic/blob/master/src/main/scala/code/snippet/EventOps.scala
 * @see    : https://github.com/jeppenejsum/lift-crud/blob/master/src/main/scala/code/snippet/Order.scala
 * @see    : https://github.com/lift/examples/blob/master/combo/example/src/main/scala/net/liftweb/example/snippet/FormWithAjax.scala
 */

class CustomerController extends StatefulSnippet {

    private var firstName = ""  
    private var lastName  = ""
    private val from = S.referer openOr "/"
 
    def dispatch = {
             case _ => render _
    }

    def render(xhtml: NodeSeq): NodeSeq = {

      //
      def validateAndProcessSubmit() {
        val c = Customer("Prayag", "Upd")
        println(c.firstName)

       (firstName.length, lastName.length) match {
         case (f, n) if f < 2 && n < 2 => S.error("First and last names too short")
         case (f, _) if f < 2 => S.error("First name too short")
         case (_, n) if n < 2 => S.error("Last name too short")
         case _ => S.notice("Thanks!"); S.redirectTo(from)
       }
      }

      bind( "form", xhtml, 
       "firstName" -> textAjaxTest( firstName , s => firstName = s, s => {S.notice("First name " + s); Noop}),
       "lastName"  -> textAjaxTest( lastName ,  s => lastName  = s, s => {S.notice("Last name "  + s); Noop}),
       "submit"    -> submit( "Save 2 ES" , validateAndProcessSubmit _)
     )
    }

}
