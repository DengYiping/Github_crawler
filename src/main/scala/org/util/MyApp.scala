package org.util

/**
  * Created by Scott on 6/24/16.
  */
import akka.actor._
import org.crawler.SearchActor

object MyApp extends App{
  implicit val system = ActorSystem("git")
  system.actorOf(Props(new SearchActor))
}
