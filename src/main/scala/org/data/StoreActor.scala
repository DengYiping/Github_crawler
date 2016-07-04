package org.data

import akka.actor.{ActorLogging, Actor}

/**
  * Created by Scott on 6/24/16.
  */
case class InsertURL(table:String, url:String)
case object Count
class StoreActor extends Actor with ActorLogging{
  var count = 0
  def receive = {
    case InsertURL(table,url) =>
      DAO.insert(table,url)
      log.info("Data comes:" + url)
      count = count + 1
    case Count => sender() ! count
    case _ =>
  }
}
