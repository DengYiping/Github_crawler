package org.util

/**
  * Created by Scott on 6/24/16.
  */

import java.net.HttpCookie
import scalaj.http._

case class HTTPexception(e:String) extends Throwable

trait GetRequest{
  private val reg_200 = """.*(200).*""".r
  private val user_agent = "Mozilla/5.0 (Macintosh; U; Intel Mac OS X 10.4; en-US; rv:1.9.2.2) Gecko/20100316 Firefox/3.6.2"

  def getRequest(url:String, cookies:Option[Map[String,String]]): String = {
    val javaCookies = cookies match {
      case Some(x) => x.map(y => new HttpCookie(y._1,y._2)).toSeq
      case None => Seq()
    }
    val request = Http(url)
      .header("User-Agent",user_agent)
      .cookies(javaCookies)
    val response = try{
      request.asString
    }catch{
      case _:Throwable => throw new HTTPexception("Error in perform HTTP request due to network issue")
    }
    if(response.isSuccess)
      reg_200 findFirstIn response.statusLine match{
        case Some(x) => response.body
        case None => throw new HTTPexception("Status code error")
      }
    else
      throw new HTTPexception("Error in get data, maybe it is a bad URL")
  }
}
