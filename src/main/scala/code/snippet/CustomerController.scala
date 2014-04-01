package code.snippet

import net.liftweb._
import net.liftweb.http._
import util._
import common._
import Helpers._
import TimeHelpers._
import net.liftweb.common.Logger
import net.liftweb.mapper._
import scala.xml._
//import code.model.Customer

/*
 * this class provides the snippets that back the Customer CRUD 
 * pages (List, Create, View, Edit, Delete)
 * @author : prayagupd
 * @see    : http://simply.liftweb.net/index-4.2.html
 * @see    : https://github.com/dph01/lift-CRUDBasic/blob/master/src/main/scala/code/snippet/EventOps.scala
 * @see    : https://github.com/jeppenejsum/lift-crud/blob/master/src/main/scala/code/snippet/Order.scala
 */
object CustomerController{
  
  // called from /customer/create.html
  def create = {
    var firstName = ""  
 
    // processSubmit is called by the both the create and edit forms when the user clicks submit
    // If the entered data passes validation then the user is redirected to the List page, 
    // otherwise the form on which the user clicked submit is reloaded 
    def processSubmit() = {
        // give the user feedback and
      //if (!Option(firstName).getOrElse("").isEmpty){
        S.notice("Customer Name: " + firstName)
        //TODO zazzercodeService.save(customer)

        // redirect to the home page
        //TODO
        // S.seeOther("/customer/list")
        S.redirectTo("/")
      //}
    }

    // takes the incoming HTML elements with the name attribute equal to "firstName"    
    "firstName=firstName" #> SHtml.onSubmit(firstName = _) & _
     // set the first name
    
    // This registers a function for Lift to call when the user clicks submit
    "type=submit" #> SHtml.onSubmitUnit(processSubmit)
  }
}
