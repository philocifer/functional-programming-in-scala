package recfun

object Main {
  def main(args: Array[String]) {
    println("Pascal's Triangle")
    for (row <- 0 to 10) {
      for (col <- 0 to row)
        print(pascal(col, row) + " ")
      println()
    }
  }

  /**
   * Exercise 1
   */
    def pascal(c: Int, r: Int): Int = {
      if (c == 0) 1
      else if (c == r) 1
      else pascal(c, r-1) + pascal(c-1, r-1)
    }
  
  /**
   * Exercise 2
   */
    def balance(chars: List[Char]): Boolean = {
      def balanceRecursive(chars: List[Char], balancedParenCount: Int): Boolean = {
        if (chars.isEmpty)
          balancedParenCount == 0
        else
          if (chars.head.equals('('))
            balanceRecursive(chars.tail, balancedParenCount + 1)
          else
            if (chars.head.equals(')'))
              if (balancedParenCount == 0)
                false
              else
                balanceRecursive(chars.tail, balancedParenCount - 1)
            else
              balanceRecursive(chars.tail, balancedParenCount)
      }
      balanceRecursive(chars, 0)
    }
  
  /**
   * Exercise 3
   */
    def countChange(money: Int, coins: List[Int]): Int = {
      if (money == 0)
        1
      else if (money > 0 && !coins.isEmpty)
        countChange(money - coins.head, coins) + countChange(money, coins.tail)
      else
        0
    }
  }
