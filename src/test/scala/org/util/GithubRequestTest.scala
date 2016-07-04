package org.util

import org.http.GithubRequest
import org.scalatest.{Matchers, FlatSpec}

/**
  * Created by Scott on 6/24/16.
  */
class GithubRequestTest extends FlatSpec with Matchers with GithubRequest{
  val result = initial_search("python","c")
  "A github requester" should "be able to fetch python results" in{
    result.length shouldBe > (0)
  }
  it should "be able to get the max page number" in{
    get_max_page(result) shouldBe > (0)
  }
  it should "list all the pages" in{
    val url = generate_search("python","Moura")(0)
    val new_result = getRequest(url,None)
    new_result.length shouldBe >(0)
  }
}
