package quickcheck

import org.scalacheck._
import Arbitrary._
import Gen._
import Prop._

abstract class QuickCheckHeap extends Properties("Heap") with IntHeap {

  lazy val genHeap: Gen[H] = for {
    x <- arbitrary[Int]
    h <- oneOf(genHeap, const(empty))
  } yield insert(x, h)

  implicit lazy val arbHeap: Arbitrary[H] = Arbitrary(genHeap)

  property("min1") = forAll { (a: Int) =>
    val h = insert(a, empty)
    findMin(h) == a
  }

  property("gen1") = forAll { (h: H) =>
    val m = if (isEmpty(h)) 0 else findMin(h)
    findMin(insert(m, h)) == m
  }

  property("smallest of two elements") = forAll { (a: Int, b: Int) =>
    val h1 = insert(a, empty)
    val h2 = insert(b, h1)
    findMin(h2) == a.min(b)
  }

  property("delete minimum in empty heap") = forAll { (a: Int) =>
    val h1 = insert(a, empty)
    val h2 = deleteMin(h1)
    h2 == empty
  }

  property("delete leaves correct heap") = forAll { (a: Int, b: Int) =>
    val h1 = insert(a, empty)
    val h2 = insert(b, h1)
    val h3 = deleteMin(h2)
    findMin(h3) == (if (a > b) a else b)
  }

  property("find and delete gives sorted sequence") = {
    def getSortedSequence(h: H, acc: List[Int]): List[Int] =
      if (isEmpty(h)) acc.reverse
      else getSortedSequence(deleteMin(h), findMin(h) :: acc)

    forAll { (h: H) =>
      val s = getSortedSequence(h, List[Int]())
      s == s.sorted
    }
  }

  property("Heaps equal after min removed") = forAll { (a: Int) =>
    val b = a / 2
    val h1 = insert(b + 2, empty)
    val h2 = insert(b, h1)
    val h3 = insert(b + 1, h2)
    val h4 = deleteMin(h3)
    h4 == insert(b + 2, insert(b + 1, empty))
  }

  property("minimum of melded heaps") = forAll { (h1: H, h2: H) =>
    val melded = meld(h1, h2)
    findMin(melded) == findMin(h1).min(findMin(h2))
  }

}
