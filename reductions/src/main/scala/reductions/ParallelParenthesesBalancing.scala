package reductions

import scala.annotation._
import org.scalameter._
import common._

object ParallelParenthesesBalancingRunner {

  @volatile var seqResult = false

  @volatile var parResult = false

  val standardConfig = config(
    Key.exec.minWarmupRuns -> 40,
    Key.exec.maxWarmupRuns -> 80,
    Key.exec.benchRuns -> 120,
    Key.verbose -> true
  ) withWarmer(new Warmer.Default)

  def main(args: Array[String]): Unit = {
    val length = 100000000
    val chars = new Array[Char](length)
    val threshold = 10000
    val seqtime = standardConfig measure {
      seqResult = ParallelParenthesesBalancing.balance(chars)
    }
    println(s"sequential result = $seqResult")
    println(s"sequential balancing time: $seqtime ms")

    val fjtime = standardConfig measure {
      parResult = ParallelParenthesesBalancing.parBalance(chars, threshold)
    }
    println(s"parallel result = $parResult")
    println(s"parallel balancing time: $fjtime ms")
    println(s"speedup: ${seqtime / fjtime}")
  }
}

object ParallelParenthesesBalancing {

  /** Returns `true` iff the parentheses in the input `chars` are balanced.
   */
  def balance(chars: Array[Char]): Boolean = {
    var balanced = true
    var balancedParenCount = 0
    var i = 0
    while (balanced && i < chars.length) {
      if (chars(i) == '(')
        balancedParenCount += 1
      else if (chars(i) == ')')
        if (balancedParenCount == 0)
          balanced = false
        else
          balancedParenCount -= 1
      i += 1
    }
    if (!balanced) false
    else balancedParenCount == 0
  }

  /** Returns `true` iff the parentheses in the input `chars` are balanced.
   */
  def parBalance(chars: Array[Char], threshold: Int): Boolean = {

    def traverse(idx: Int, until: Int, arg1: Int, arg2: Int): (Int, Int) = {
      var leftCount: Int = arg1
      var rightCount: Int = arg2
      var i: Int = idx
      while (i < until) {
        if (chars(i) == '(')
          leftCount += 1
        else if (chars(i) == ')')
          if (leftCount > 0)
            leftCount -= 1
          else
            rightCount += 1
        i += 1
      }
      (leftCount, rightCount)
    }

    def reduce(from: Int, until: Int): (Int, Int) = {
      if (until - from < threshold)
        traverse(from, until, 0, 0)
      else {
        val mid = from + (until - from)/2
        val (x, y) = parallel(reduce(from, mid), reduce(mid, until))
        List(x, y).reduceLeft((left, right) =>
          if (right._2 >= left._1)
            (right._1, (right._2 - left._1) + left._2)
          else
            (right._1 + (left._1 - right._2), left._2))
      }
    }

    reduce(0, chars.length) == (0,0)
  }

  // For those who want more:
  // Prove that your reduction operator is associative!

}
