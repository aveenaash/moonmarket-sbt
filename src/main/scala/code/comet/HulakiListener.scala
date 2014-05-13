package code.comet

import net.liftweb.http._
import net.liftweb.util._


class HulakiListener extends CometActor with CometListener
{
  private var msgs : String= ""

  def registerWith = HulakiListenerManager

  def render = "textarea *" #> msgs

  override def lowPriority = {
    case v : String => msgs = v;
      println("msgs value =>"+msgs);
      reRender(false)
  }

}
