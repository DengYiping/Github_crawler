package org.crawler

/**
  * Created by Scott on 6/24/16.
  */
trait LinkExtract {
  val reg = "<a href=\"(/.*/.*)\">.*</a>"
  val reg_compiled = reg.r
  def extract_link(raw_search_page:String):List[String] = {
    raw_search_page.lines.map(_.trim).withFilter(_.matches(reg)).map {
      st =>
        val reg_compiled(new_s) = st
        new_s
    }.toList
  }
}
