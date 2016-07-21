package org.crawler

import org.util.GetRequest

import scala.collection.mutable.{ArrayBuffer, ListBuffer}
import scala.util.matching.Regex

/**
  * Created by Scott on 7/4/16.
  */
trait DownloadReleaseGen extends GetRequest{
  def gen_release_links(link:String):List[String] = {
    val base = "https://github.com"
    val release_reg = "<a href=\"(.*zip)\" rel=\"nofollow\">"
    val compiled_release_reg = release_reg.r
    val project_url = base + link
    val release_url = project_url + "/releases"
    val web = getRequest(release_url,None).lines.map(_.trim).toList

    val result=  web.withFilter(_.matches(release_reg)).map{
      s =>
        val compiled_release_reg(s1) = s
        base + s1
    }.toList

    if(result.isEmpty) Nil
    else{
      val listBuf = new ListBuffer[String]
      val reg = ".*\"(https.*github.*\\/releases\\?after.*)\" rel.*"
      val reg_p = reg.r
      //println(web mkString "\n")
      val x = web.filter(_ matches reg)
      //println(x.length)
      if(x.nonEmpty){
        var reg_p(y) = x.head
        var hasNextPage = false
        do{
          val w = getRequest(y,None).lines.map(_.trim)
          listBuf ++= w.withFilter(_.matches(release_reg)).map{
            s =>
              val compiled_release_reg(s1) = s
              base + s1
          }

          val z = w.filter(_ matches reg)
          hasNextPage = z.nonEmpty
          if(hasNextPage)
            y = z.next()
        }while(hasNextPage)
      }
      listBuf ++= result
      //println(listBuf mkString "\n")
      listBuf.toList
    }
  }
}
