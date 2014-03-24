package zazzercode

/**
  * @see : http://en.wikipedia.org/wiki/Gibbs_sampling
  * @see : http://darrenjw.wordpress.com/2013/12/30/brief-introduction-to-scala-and-breeze-for-statistical-computing/
  */

object BreezeGibbs {
 
  import breeze.stats.distributions._
  import scala.math.sqrt
 
  class State(val x: Double, val y: Double)
 
  def nextIter(state: State): State = {
    val newX = Gamma(3.0, 1.0 / ((state.y) * (state.y) + 4.0)).draw()
    new State(newX, Gaussian(1.0 / (newX + 1), 1.0 / sqrt(2 * newX + 2)).draw())
  }
 
  def nextThinnedIter(state: State, left: Int): State = {
    if (left == 0) state
    else nextThinnedIter(nextIter(state), left - 1)
  }
 
  def genIters(state: State, 
	       current: Int, 
	       stop: Int, 
	       thin: Int): State = {
    if (!(current > stop)) {
      println(current + " " + state.x + " " + state.y)
      genIters(nextThinnedIter(state, thin), current + 1, stop, thin)
    } else state
  }
 
  def main(args: Array[String]) {
    println("Iter x y")
    val N    = 50000
    val thin = 1000
    genIters(new State(0.0, 0.0), 1, 50000, 1000)
  }
 
}
