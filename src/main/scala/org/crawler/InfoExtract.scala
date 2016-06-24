package org.crawler

/**
  * Created by Scott on 6/24/16.
  */
trait InfoExtract {
  def extract_info(raw_project_page:String):ProjectInfo
}
