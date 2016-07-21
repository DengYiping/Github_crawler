package org.crawler

import akka.actor.SupervisorStrategy.Restart
import akka.actor._
import akka.routing.{SmallestMailboxRoutingLogic, Router, ActorRefRoutee}
import org.data.{InsertURL, Config}
import org.util.GetRequest

import scala.util.Try

/**
  * Created by Scott on 6/24/16.
  */
class DownloadActor(dao:ActorRef) extends Actor{
  var language = Config.config.getString("crawler.start_language")

  class DownloadWorker(language:String, dao:ActorRef) extends Actor with ActorLogging with DownloadLinkGen with DownloadReleaseGen{
    def receive = {
      case s:String =>
        Try{
        //log.info("extraction start on " + s)
        gen_download_links(s)
      }.toOption match {
        case Some(e) =>
          e.foreach(dao ! InsertURL(language,_))
        case None =>
      }
        Try{
          gen_release_links(s)
        }.toOption match {
          case Some(e) =>
            e.foreach(dao ! InsertURL(language + "_re",_))
          case None =>
        }
    }
  }


  override val supervisorStrategy= OneForOneStrategy() {
    case _:Throwable => Restart
  }

  var router = {
    val routees = Vector.fill(Config.config.getInt("crawler.download_link_extractor_num")) {
      val r = context.actorOf(Props(new DownloadWorker(language,dao)))
      context watch r
      ActorRefRoutee(r)
    }
    Router(SmallestMailboxRoutingLogic(), routees)
  }
  def receive = {
    case s:String =>
      router.route(s, self)
    case Terminated(a) =>
      router = router.removeRoutee(a)
      val r = context.actorOf(Props(new DownloadWorker(language,dao)))
      context watch r
      router = router.addRoutee(r)
  }
}
