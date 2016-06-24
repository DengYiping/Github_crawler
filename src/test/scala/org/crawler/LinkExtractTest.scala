package org.crawler

import org.scalatest.{Matchers, FlatSpec}
import org.util.GetRequest

/**
  * Created by Scott on 6/24/16.
  */
class LinkExtractTest extends FlatSpec with Matchers with LinkExtract with GetRequest{
  "A link extractor" should "be able to extract github links" in{
    val test_url = "https://github.com/search?q=language%3Apython+c&p=3"
    val result = getRequest(test_url,None)
    val links = extract_link(result)
    links.length shouldBe >(0)
  }
}
