package org.crawler

import akka.actor.SupervisorStrategy.Restart
import akka.actor._
import akka.routing.{SmallestMailboxRoutingLogic, Router, ActorRefRoutee}
import org.data.{StoreActor, Config}
import org.http.GithubRequest
import org.util.Bloomfilter

import scala.util.Try

/**
  * Created by Scott on 6/24/16.
  */
case class Search(term:String)
class SearchActor extends Actor with ActorLogging with GithubRequest{
  var work_language = Config.config.getString("crawler.start_language")

  val term_actorRef = context.actorOf(Props(new TermExtractActor(self)))
  val dao = context.actorOf(Props(new StoreActor))
  val download_acotrRef = context.actorOf(Props(new DownloadActor(dao)))
  val link_ext = context.actorOf(Props(new LinkExtractActor(term_actorRef,download_acotrRef)))

  var filter = Bloomfilter[String](1000000000, 4)
  override val supervisorStrategy= OneForOneStrategy() {
    case _:Throwable => Restart
  }

  var router = {
    val routees = Vector.fill(Config.config.getInt("crawler.searcher_num")){
      val r = context.actorOf(Props(new SearchWorker(work_language,link_ext)))
      context watch r
      ActorRefRoutee(r)
    }
    Router(SmallestMailboxRoutingLogic(), routees)
  }

  def receive = {
    case Search(s) =>
      if(!filter(s))
        router.route(s, self)
    case Terminated(t) =>
      router = router.removeRoutee(t)
      val r = context.actorOf(Props(new SearchWorker(work_language,link_ext)))
      context watch r
      router = router.addRoutee(r)
  }
  self ! Search("term")
  self ! Search("hadoop")
  self ! Search("wisdom")
  self ! Search("unix")
  self ! Search("shadow")
}

class SearchWorker(var language:String, link_ext:ActorRef) extends Actor with ActorLogging with GithubRequest{
  def receive = {
    case s:String =>
      Try{
        Thread.sleep(Config.delay)
        generate_search(language,s)
      }.toOption match {
        case Some(s1) =>
          s1.foreach(link_ext ! _)
          log.info(s1.length + " link extracted from term " + s)
        case None =>
          log.warning("failed to extract from term " + s)
          Thread.sleep(Config.failed_delay)
      }
  }
}