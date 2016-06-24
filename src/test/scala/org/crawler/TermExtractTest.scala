package org.crawler

import org.scalatest.{Matchers, FlatSpec}
import org.util.{Bloomfilter, GetRequest}

/**
  * Created by Scott on 6/24/16.
  */
class TermExtractTest extends FlatSpec with Matchers with TermExtract with GetRequest with LinkExtract {
  val filter = Bloomfilter[String](10000000,3)
  "Term extractor" should "extract terms" in{
    val terms = extract_term("/bfontaine/term2048")
    terms.length shouldBe >=(0)
  }
}
