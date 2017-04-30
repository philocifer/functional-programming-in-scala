package funsets

import org.scalatest.FunSuite


import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner

/**
 * This class is a test suite for the methods in object FunSets. To run
 * the test suite, you can either:
 *  - run the "test" command in the SBT console
 *  - right-click the file in eclipse and chose "Run As" - "JUnit Test"
 */
@RunWith(classOf[JUnitRunner])
class FunSetSuite extends FunSuite {

  /**
   * Link to the scaladoc - very clear and detailed tutorial of FunSuite
   *
   * http://doc.scalatest.org/1.9.1/index.html#org.scalatest.FunSuite
   *
   * Operators
   *  - test
   *  - ignore
   *  - pending
   */

  /**
   * Tests are written using the "test" operator and the "assert" method.
   */
  // test("string take") {
  //   val message = "hello, world"
  //   assert(message.take(5) == "hello")
  // }

  /**
   * For ScalaTest tests, there exists a special equality operator "===" that
   * can be used inside "assert". If the assertion fails, the two values will
   * be printed in the error message. Otherwise, when using "==", the test
   * error message will only say "assertion failed", without showing the values.
   *
   * Try it out! Change the values so that the assertion fails, and look at the
   * error message.
   */
  // test("adding ints") {
  //   assert(1 + 2 === 3)
  // }


  import FunSets._

  test("contains is implemented") {
    assert(contains(x => true, 100))
  }

  /**
   * When writing tests, one would often like to re-use certain values for multiple
   * tests. For instance, we would like to create an Int-set and have multiple test
   * about it.
   *
   * Instead of copy-pasting the code for creating the set into every test, we can
   * store it in the test class using a val:
   *
   *   val s1 = singletonSet(1)
   *
   * However, what happens if the method "singletonSet" has a bug and crashes? Then
   * the test methods are not even executed, because creating an instance of the
   * test class fails!
   *
   * Therefore, we put the shared values into a separate trait (traits are like
   * abstract classes), and create an instance inside each test method.
   *
   */

  trait TestSets {
    val s0 = singletonSet(0)
    val s1 = singletonSet(1)
    val s2 = singletonSet(2)
    val s3 = singletonSet(3)
    val s4 = singletonSet(4)
    val s5 = singletonSet(5)
    val s7 = singletonSet(7)
    val s1000 = singletonSet(1000)
    val sNeg1000 = singletonSet(-1000)
  }

  /**
   * This test is currently disabled (by using "ignore") because the method
   * "singletonSet" is not yet implemented and the test would fail.
   *
   * Once you finish your implementation of "singletonSet", exchange the
   * function "ignore" by "test".
   */
  test("singletonSet(1) contains 1") {

    /**
     * We create a new instance of the "TestSets" trait, this gives us access
     * to the values "s1" to "s3".
     */
    new TestSets {
      /**
       * The string argument of "assert" is a message that is printed in case
       * the test fails. This helps identifying which assertion failed.
       */
      assert(contains(s1, 1), "Singleton")
    }
  }

  test("union contains all elements of each set") {
    new TestSets {
      val s = union(s1, s2)
      assert(contains(s, 1), "Union 1")
      assert(contains(s, 2), "Union 2")
      assert(!contains(s, 3), "Union 3")
    }
  }

  test("intersect contains elements in common between sets") {
    new TestSets {
      val s = union(s1, s2)
      val t = union(s2, s3)
      val u = intersect(s, t)
      assert(!contains(u, 1), "Intersect 1")
      assert(contains(u, 2), "Intersect 2")
      assert(!contains(u, 3), "Intersect 3")
    }
  }

  test("diff 1 contains elements not in common between sets") {
    new TestSets {
      val s = union(union(union(union(union(s1, s3), s4), s5), s7), s1000)
      val t = union(union(union(s1, s2), s3), s4)
      val u = diff(s, t)
      assert(!contains(u, 1), "Diff 1")
      assert(contains(u, 2), "Diff 2")
      assert(!contains(u, 3), "Diff 3")
      assert(!contains(u, 4), "Diff 4")
      assert(contains(u, 5), "Diff 5")
      assert(contains(u, 7), "Diff 7")
      assert(contains(u, 1000), "Diff 1000")
    }
  }

  test("diff 2 contains elements not in common between sets") {
    new TestSets {
      val s = union(union(union(s1, s2), s3), s4)
      val t = union(sNeg1000, s0)
      val u = diff(s, t)
      assert(contains(u, 1), "Diff 1")
      assert(contains(u, 2), "Diff 2")
      assert(contains(u, 3), "Diff 3")
      assert(contains(u, 4), "Diff 4")
      assert(contains(u, 0), "Diff 0")
      assert(contains(u, -1000), "Diff -1000")
    }
  }

  test("filter contains elements in common between sets") {
    new TestSets {
      val s = union(s1, s2)
      val t = union(s, s3)
      val u = union(s1, s2)
      val v = filter(t, elem => u(elem))
      assert(contains(v, 1), "Filter 1")
      assert(contains(v, 2), "Filter 2")
      assert(!contains(v, 3), "Filter 3")
    }
  }

  test("forall contains all elements in bounded set") {
    new TestSets {
      val s = union(s1, s2)
      val t = union(s, s3)
      val v = forall(t, x => x >= 1 && x <= 3)
      assert(v, "forall 1-3")
    }
  }

  test("exists contains at least one element in bounded set") {
    new TestSets {
      val s = union(s1, s2)
      val t = union(s, s3)
      val v = exists(t, x => x >= 2 && x <= 4)
      assert(v, "exists 2-4 in 1-3")
    }
  }

  test("map transforms elements of a set") {
    new TestSets {
      val s = union(s1, s2)
      val t = union(s, s3)
      val v = map(t, x => x * 2)
      assert(contains(v, 2), "Map 1 to 1 * 2")
      assert(contains(v, 4), "Map 2 to 2 * 2")
      assert(contains(v, 6), "Map 3 to 3 * 2")
    }
  }

}
