package org.crawler

import akka.actor.SupervisorStrategy.Restart
import akka.actor._
import akka.routing.{SmallestMailboxRoutingLogic, Router, ActorRefRoutee}
import org.data.Config
import org.http.GithubRequest
import org.util.{Bloomfilter, GetRequest}

import scala.util.Try

/**
  * Created by Scott on 6/24/16.
  */
class LinkExtractActor(term_ext:ActorRef, downloader:ActorRef) extends Actor with ActorLogging with LinkExtract with GetRequest{
  class LinkExtractWorker(term_ext:ActorRef, downloader:ActorRef, filter:(String =>  Boolean)) extends Actor with ActorLogging with LinkExtract with GetRequest{
    def receive = {
      case s:String =>Try{
        Thread.sleep(Config.delay)
        val result = getRequest(s, Some(Config.session))
        extract_link(result)
      }.toOption match {
        case Some(e) =>
          val r = e.filterNot(filter)
          r.foreach(term_ext ! Project(_))
          r.foreach(downloader ! _)
        case None =>
          Thread.sleep(Config.failed_delay)
      }
    }
  }

  val filter = Bloomfilter[String](100000000,4)

  override val supervisorStrategy= OneForOneStrategy() {
    case _:Throwable => Restart
  }

  var router = {
    val routees = Vector.fill(Config.config.getInt("crawler.link_extractor_num")) {
      val r = context.actorOf(Props(new LinkExtractWorker(term_ext,downloader,filter)))
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
      val r = context.actorOf(Props(new LinkExtractWorker(term_ext,downloader,filter)))
      context watch r
      router = router.addRoutee(r)
  }
}
