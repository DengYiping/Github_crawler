package org.crawler

import akka.actor.SupervisorStrategy.Restart
import akka.actor._
import akka.routing.{Router, SmallestMailboxRoutingLogic, ActorRefRoutee}
import org.data.Config
import org.util.Bloomfilter

import scala.util.Try

/**
  * Created by Scott on 6/24/16.
  */
case class Project(s:String)
case object ClearFilter

class TermExtractActor(searcher:ActorRef) extends Actor with ActorLogging with TermExtract{

  /**
    * worker class
    */
  class TermExtractWorker(searcher:ActorRef) extends Actor with ActorLogging with TermExtract{
    def receive = {
      case Project(s) =>
        log.info("extract term from:" + s)
        Try(extract_term(s)).toOption match{
          case Some(e) =>
            e.foreach(searcher ! Search(_))
            log.info(e.length + " term extracted")
          case None =>
        }
    }
  }

  /**
    * normal strategy
    */
  override val supervisorStrategy= OneForOneStrategy() {
    case _:Throwable => Restart
  }

  var router = {
    val routees = Vector.fill(Config.config.getInt("crawler.term_extractor_num")) {
      val r = context.actorOf(Props(new TermExtractWorker(context.parent)))
      context watch r
      ActorRefRoutee(r)
    }
    Router(SmallestMailboxRoutingLogic(), routees)
  }
  def receive = {
    case s:Project =>
      router.route(s, self)
    case Terminated(a) =>
      router = router.removeRoutee(a)
      val r = context.actorOf(Props(new TermExtractWorker(context.parent)))
      context watch r
      router = router.addRoutee(r)
  }
}