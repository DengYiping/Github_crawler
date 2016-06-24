package org.crawler

import org.scalatest.{Matchers, FlatSpec}

/**
  * Created by Scott on 6/24/16.
  */
class DownloadLinkGenTest extends FlatSpec with Matchers with DownloadLinkGen with LinkExtract{
  "Link extractor" should "extract links" in {
    val test_url = "https://github.com/search?q=language%3Apython+c&p=1"
    val result = getRequest(test_url,None)
    val links = extract_link(result)
    val download_links = gen_download_links(links(0))
    download_links.length shouldBe >(0)
  }
}
