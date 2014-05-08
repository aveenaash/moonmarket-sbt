package bootstrap.liftweb

import net.liftweb._
import util._
import Helpers._

import common._
import http._
import js.jquery.JQueryArtifacts
import sitemap._
import Loc._
import mapper._

import code.model._
//import net.liftmodules.JQueryModule
import net.liftmodules.{FoBo, JQueryModule}

import zazzercode.RestController

import net.liftmodules.amqp.AMQPAddListener
import code.comet.ChatServer
import code.comet.Rabbit.RemoteReceiver

/**
 * A class that's instantiated early and run.  It allows the application
 * to modify lift's environment
 */

class Boot {
  def boot {
    if (!DB.jndiJdbcConnAvailable_?) {
      val vendor = 
	new StandardDBVendor(Props.get("db.driver") openOr "org.h2.Driver",
			     Props.get("db.url") openOr 
			     "jdbc:h2:lift_proto.db;AUTO_SERVER=TRUE",
			     Props.get("db.user"), Props.get("db.password"))

      LiftRules.unloadHooks.append(vendor.closeAllConnections_! _)

      DB.defineConnectionManager(DefaultConnectionIdentifier, vendor)
    }

    // Use Lift's Mapper ORM to populate the database
    // you don't need to use Mapper to use Lift... use
    // any ORM you want
    Schemifier.schemify(true, Schemifier.infoF _, User)

    // FoBo init params 
    // https://github.com/karma4u101/FoBo-Demo/blob/master/fobo-lift-template-demo/src/main/scala/bootstrap/liftweb/Boot.scala
    FoBo.InitParam.JQuery=FoBo.JQuery191 //1102
    FoBo.InitParam.ToolKit=FoBo.Foundation215
    FoBo.InitParam.ToolKit=FoBo.PrettifyJun2011
    FoBo.InitParam.ToolKit=FoBo.JQueryMobile110
    FoBo.InitParam.ToolKit=FoBo.DataTables190
    FoBo.InitParam.ToolKit=FoBo.Knockout210
    FoBo.InitParam.ToolKit=FoBo.Bootstrap232
    FoBo.InitParam.ToolKit=FoBo.FontAwesome321
    FoBo.InitParam.ToolKit=FoBo.AngularJS1211      //The core files 
    FoBo.InitParam.ToolKit=FoBo.AJSUIBootstrap020 //Angular UI Bootstrap
    FoBo.InitParam.ToolKit=FoBo.AJSNGGrid204      //Angular NG-Grid    
    FoBo.InitParam.ToolKit=FoBo.Pace0415
    FoBo.init()  

    LiftRules.useXhtmlMimeType = false

    // where to search snippet
    LiftRules.addToPackages("code")

    // Build SiteMap
    def sitemap = SiteMap(
      Menu.i("Home")             / "index" >> User.AddUserMenusAfter, // the simple way to declare a menu
      Menu("Comet Moon Chat")    / "chat",
      Menu("Moon Customer")      / "customer/create",
      Menu("Moon Customer>Edit") / "customer/edit",
      Menu("Hulaki")             / "hulaki/hulaki",
      Menu("Hulaki list")        / "hulaki/list",
      Menu("Moon Market")        / "market",
      // more complex because this menu allows anything in the
      // /static path to be visible
      Menu(Loc("Static", Link(List("static"), true, "/static/index"), 
	       "Static Content")))

    def sitemapMutators = User.sitemapMutator

    // set the sitemap.  Note if you don't want access control for
    // each page, just comment this line out.
    LiftRules.setSiteMapFunc(() => sitemapMutators(sitemap))

    //Init the jQuery module, see http://liftweb.net/jquery for more information.
    LiftRules.jsArtifacts = JQueryArtifacts
    JQueryModule.InitParam.JQuery=JQueryModule.JQuery172
    JQueryModule.init()

    //Show the spinny image when an Ajax call starts
    LiftRules.ajaxStart =
      Full(() => LiftRules.jsArtifacts.show("ajax-loader").cmd)
    
    // Make the spinny image go away when it ends
    LiftRules.ajaxEnd =
      Full(() => LiftRules.jsArtifacts.hide("ajax-loader").cmd)

    // Force the request to be UTF-8
    LiftRules.early.append(_.setCharacterEncoding("UTF-8"))

    // What is the function to test if a user is logged in?
    LiftRules.loggedInTest = Full(() => User.loggedIn_?)

    // Use HTML5 for rendering
    LiftRules.htmlProperties.default.set((r: Req) =>
      new Html5Properties(r.userAgent))    

    /**
      * hook RestController
      */
    LiftRules.dispatch.append(RestController)
    LiftRules.statelessDispatch.append(RestController)

    // Make a transaction span the whole HTTP request
    S.addAround(DB.buildLoanWrapper)

    RemoteReceiver ! AMQPAddListener(ChatServer)
  }
}
