package org.crawler

import org.util.GetRequest

/**
  * Created by Scott on 6/24/16.
  */
trait DownloadLinkGen extends GetRequest{
  def gen_download_links(link:String):List[String] = {
    val base = "https://github.com"
    val branch_reg = "<div class=\".*\" data-branch-name=\"(.*)\">"
    val compiled_branch_reg = branch_reg.r
    val project_url = base + link
    val branches_url = project_url + "/branches"
    getRequest(branches_url,None).lines.map(_.trim).withFilter(_.matches(branch_reg)).map{
      s =>
        val compiled_branch_reg(s1) = s
        s1
    }.map{
      s => project_url + "/archive/" + s + ".zip"
    }.toList
  }
}
