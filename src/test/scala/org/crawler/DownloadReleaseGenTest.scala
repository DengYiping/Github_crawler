package org.crawler

import org.scalatest.{Matchers, FlatSpec}

/**
  * Created by Scott on 7/4/16.
  */
class DownloadReleaseGenTest extends FlatSpec with Matchers with DownloadReleaseGen{
  it should "fetch links" in{
    gen_release_links("/ddfreyne/cri").length should be > (0)
  }
}
