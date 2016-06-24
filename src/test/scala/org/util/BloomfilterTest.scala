package org.util

import  org.scalatest.Matchers
import org.scalatest.FlatSpec
/**
  * Created by Scott on 6/24/16.
  */
class BloomfilterTest extends FlatSpec with Matchers {
  "A bloom filter" should "be able to filter repected object" in{
    val filter = Bloomfilter[String](10000000,3)
    filter("hello") shouldBe (false)
    filter("hello") shouldBe (true)
  }
}
