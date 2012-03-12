import sbt._
import Keys._
import PlayProject._

trait Resolvers {
  val typesafe = "typesafe.com" at "http://repo.typesafe.com/typesafe/releases/"
  val iliaz = "iliaz.com" at "http://scala.iliaz.com/"
}

trait Dependencies {
  val scalaz = "org.scalaz" %% "scalaz-core" % "6.0.4"
  val specs2 = "org.specs2" %% "specs2" % "1.8.2"
}

object ApplicationBuild extends Build with Resolvers with Dependencies {

  val appName = "scalex-http"
  val appVersion = "1.0-SNAPSHOT"

  lazy val scalex = uri("git://github.com/ornicar/scalex#master")
  //lazy val scalex = uri("/home/thib/scalex")

  val main = PlayProject(appName, appVersion, mainLang = SCALA).settings(
    resolvers ++= Seq(typesafe, iliaz),
    libraryDependencies ++= Seq(scalaz),
    scalacOptions := Seq("-deprecation", "-unchecked")) dependsOn scalex
}
