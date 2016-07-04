package org.crawler

import org.util.GetRequest

/**
  * Created by Scott on 6/24/16.
  */
import org.data.Config
trait TermExtract extends GetRequest {
  def extract_term(link:String):List[String] = {
    val base = "https://github.com"
    val project_url = base + link
    val reg = "<p>(.*)</p>"
    val compiled_reg = reg.r
    getRequest(project_url,Some(Config.session)).lines.map(_.trim).withFilter(_.matches(reg)).map{
      s =>
        val compiled_reg(s1) = s
        s1
    }.flatMap(_.split("\\s+")).filterNot(_.charAt(0) == '<').toList
  }
}
