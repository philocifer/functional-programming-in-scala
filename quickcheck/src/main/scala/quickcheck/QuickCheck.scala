package quickcheck

import common._

import org.scalacheck._
import Arbitrary._
import Gen._
import Prop._

abstract class QuickCheckHeap extends Properties("Heap") with IntHeap {

  lazy val genHeap: Gen[H] =
    for {
      i <- arbitrary[A]
      h <- oneOf(const(empty), genHeap)
    } yield insert(i, h)

  implicit lazy val arbHeap: Arbitrary[H] = Arbitrary(genHeap)

  property("gen1") = forAll { (h: H) =>
    val m = if (isEmpty(h)) 0 else findMin(h)
    findMin(insert(m, h)) == m
  }

  property("min1") = forAll { a: A =>
    val h = insert(a, empty)
    findMin(h) == a
  }

  property("smallest of 2 elements is min") = forAll {(a: A, b: A) =>
    val h = insert(a, insert(b, empty))
    if (a < b) findMin(h) == a && findMin(deleteMin(h)) == b
    else findMin(h) == b && findMin(deleteMin(h)) == a
  }

  property("delete min is empty") = forAll { a: A =>
    val h = insert(a, empty)
    isEmpty(deleteMin(h))
  }

  property("sorted sequence") = forAll { (a: A, b: A, c: A) =>
    val h = insert(a, insert(b, insert(c, empty)))
    val sorted = List(a, b, c).sorted
    sorted(0) == findMin(h) && sorted(1) == findMin(deleteMin(h)) && sorted(2) == findMin(deleteMin(deleteMin(h)))
  }

  property("min meld") = forAll { (h1: H, h2: H) =>
    val min = Math.min(findMin(h1), findMin(h2))
    findMin(meld(h1, h2)) == min
  }

}
