package org.data

/**
  * Created by Scott on 6/24/16.
  */
import com.typesafe.config.ConfigFactory
object Config {
  val config = ConfigFactory.load()
  lazy val start_language = config.getString("crawler.start_language")
  val session = Map(
    "tz" -> config.getString("session.tz"),
    "_gh_sess" -> config.getString("session._gh_sess"),
    "user_session" -> config.getString("session.user_session"),
    "dotcom_user" -> config.getString("session.dotcom_user"),
    "logged_in" -> "yes",
    "__utmc" -> config.getString("session.__utmc")
  )
  val delay = config.getLong("crawler.delay")
  val failed_delay = config.getLong("crawler.failed_delay")
}
