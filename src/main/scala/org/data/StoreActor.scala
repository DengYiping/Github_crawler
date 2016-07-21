package org.data

import akka.actor.{ActorLogging, Actor}

/**
  * Created by Scott on 6/24/16.
  */
case class InsertURL(table:String, url:String)
case object Count
class StoreActor extends Actor with ActorLogging{
  var count = 0
  var language = Config.config.getString("crawler.start_language")
  DAO.createStandardTable(language)
  DAO.createStandardTable(language + "_re")

  def receive = {
    case InsertURL(table,url) =>
      DAO.insert(table,url)
      log.info(s"Store to table $table:" + url)
      count = count + 1
    case Count => sender() ! count
    case _ =>
  }
}
