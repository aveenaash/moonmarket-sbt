package code.snippet


import net.liftweb.http.SHtml
import net.liftweb.http.js.JsCmds.SetValById
import code.comet.HulakiListenerManager

object HulakiController{

  def render = {
    SHtml.onSubmit( s =>
    {

      HulakiListenerManager ! s
      //SetValById("request", s)
    })
  }
}