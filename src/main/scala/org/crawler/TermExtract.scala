package org.crawler

import org.util.GetRequest

/**
  * Created by Scott on 6/24/16.
  */
trait TermExtract extends GetRequest {
  val filter:(String =>Boolean)
  def extract_term(link:String):List[String] = {
    val base = "https://github.com"
    val project_url = base + link
    val reg = "<p>(.*)</p>"
    val compiled_reg = reg.r
    getRequest(project_url,None).lines.map(_.trim).withFilter(_.matches(reg)).map{
      s =>
        val compiled_reg(s1) = s
        s1
    }.flatMap(_.split("\\s+")).withFilter(!filter(_)).toList
  }
}
