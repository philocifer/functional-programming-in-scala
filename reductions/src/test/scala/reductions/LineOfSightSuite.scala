package reductions

import java.util.concurrent._
import scala.collection._
import org.scalatest.FunSuite
import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner
import common._
import java.util.concurrent.ForkJoinPool.ForkJoinWorkerThreadFactory

@RunWith(classOf[JUnitRunner]) 
class LineOfSightSuite extends FunSuite {
  import LineOfSight._
  test("lineOfSight should correctly handle an array of size 4") {
    val output = new Array[Float](4)
    lineOfSight(Array[Float](0f, 1f, 8f, 9f), output)
    assert(output.toList == List(0f, 1f, 4f, 4f))
  }


  test("upsweepSequential should correctly handle the chunk 1 until 4 of an array of 4 elements") {
    val res = upsweepSequential(Array[Float](0f, 1f, 8f, 9f), 1, 4)
    assert(res == 4f)
  }


  test("downsweepSequential should correctly handle a 4 element array when the starting angle is zero") {
    val output = new Array[Float](4)
    downsweepSequential(Array[Float](0f, 1f, 8f, 9f), output, 0f, 1, 4)
    assert(output.toList == List(0f, 1f, 4f, 4f))
  }

  test("upsweep and downsweep should work 1") {
    val input = Array[Float](0f, 1f, 4f, 9f, 16f, 10f)
    val output = new Array[Float](6)
    val tree: Tree = Node(Node(Leaf(0,1,0f),Node(Leaf(1,2,1f),Leaf(2,3,2f))),Node(Leaf(3,4,3f),Node(Leaf(4,5,4f),Leaf(5,6,2f))))
    assert(upsweep(input,0,6,1) == tree)
    downsweep(input,output,0,tree)
    assert(output.toList == List(0f, 1f, 2f, 3f, 4f, 4f))
  }

  test("upsweep and downsweep should work 2") {
    val input = Array[Float](0f, 1f, 4f, 9f, 16f, 10f)
    val output = new Array[Float](6)
    val tree: Tree = Node(Leaf(0,3,2f),Leaf(3,6,4f))
    assert(upsweep(input,0,6,3) == tree)
    downsweep(input,output,0,tree)
    assert(output.toList == List(0f, 1f, 2f, 3f, 4f, 4f))
  }

  test("upsweep and downsweep should work 3") {
    val input = Array[Float](0f, 1f)
    val output = new Array[Float](2)
    val tree: Tree = Node(Leaf(0,1,0f),Leaf(1,2,1f))
    assert(upsweep(input,0,2,1) == tree)
    downsweep(input,output,0,tree)
    assert(output.toList == List(0f, 1f))
  }
}

