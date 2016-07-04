package org.http

import org.data.Config
import org.util.GetRequest

import scala.util.Try

/**
  * Created by Scott on 6/24/16.
  */
trait GithubRequest extends GetRequest {
  val max_page_reg = ".*<a href=\"/search\\?p=(\\d+).*"
  val compiled_maxpage_reg = max_page_reg.r
  private val search_base_url = "https://github.com/search?q="
  def initial_search(language:String, term:String):String = {
    val language_query = "language%3A" + language
    val full_url = search_base_url + language_query + "+" + term
    getRequest(full_url,Some(Config.session))
  }
  def get_max_page(raw_page:String):Int = {
    Try(raw_page.lines.map(_.trim).withFilter(_.matches(max_page_reg)).map{
      s =>
        val compiled_maxpage_reg(num) = s
        num.toInt
    }.max).toOption match {
      case Some(e) => e
      case None => 1
    }
  }
  def generate_search(language:String, term:String):List[String] = {
    val result = initial_search(language,term)
    val max_page = get_max_page(result)
    val language_query = "language%3A" + language
    val full_url = search_base_url + language_query + "+" + term
    (1 to max_page).map(full_url + "&p=" + _.toString).toList
  }
}
