name := "sbt_template"
organization := "org.qq"
version := "0.1"
scalaVersion := "2.11.7"
resolvers += "spray repo" at "http://repo.spray.io"
libraryDependencies += "org.scalatest" % "scalatest_2.11" % "2.2.4" % "test"
libraryDependencies += "org.scalaj" % "scalaj-http_2.11" % "2.2.1"
//spray stack
libraryDependencies ++= {
  val akkaV = "2.4.1"
  val sprayV = "1.3.3"
  Seq(
    "io.spray" %% "spray-can" % sprayV,
    "io.spray" %% "spray-http" % sprayV,
    "io.spray" %% "spray-httpx" % sprayV,
    "io.spray" %% "spray-routing" % sprayV,
    "io.spray" %% "spray-servlet" % sprayV,
    "io.spray" %% "spray-util" % sprayV,
    "io.spray" %% "spray-io" % sprayV,
    "io.spray" %% "spray-client" % sprayV,
    "io.spray" %% "spray-caching" % sprayV,
    "io.spray" %% "spray-testkit" % sprayV % "test",
    "io.spray" %% "spray-json" % "1.3.2",
    "com.typesafe.akka" %% "akka-actor" % akkaV,
    "com.typesafe.akka" %% "akka-testkit" % akkaV % "test",
    "org.scalikejdbc" %% "scalikejdbc"       % "2.4.1",
    "com.h2database"  %  "h2"                % "1.4.191",
    "ch.qos.logback"  %  "logback-classic"   % "1.1.7"

  )
}
libraryDependencies += "com.typesafe" % "config" % "1.3.0"
libraryDependencies += "mysql" % "mysql-connector-java" % "5.1.38"
