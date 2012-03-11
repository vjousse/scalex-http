import sbt._
import Keys._

trait Resolvers {
  val typesafe = "typesafe.com" at "http://repo.typesafe.com/typesafe/releases/"
  val iliaz = "iliaz.com" at "http://scala.iliaz.com/"
}

trait Dependencies {
  val playmini = "com.typesafe" %% "play-mini" % "2.0-RC4"
  val scalaz = "org.scalaz" %% "scalaz-core" % "6.0.4"
  val specs2 = "org.specs2" %% "specs2" % "1.8.2"
}

object ScalexHttpBuild extends Build with Resolvers with Dependencies {

  val buildSettings = Defaults.defaultSettings ++ Seq(
    version := "0.1",
    scalaVersion := "2.9.1",
    libraryDependencies in Test := Seq(specs2),
    resolvers := Seq(typesafe, iliaz),
    shellPrompt := {
      (state: State) ⇒ "%s> ".format(Project.extract(state).currentProject.id)
    },
    scalacOptions := Seq("-deprecation", "-unchecked"))

  lazy val scalex = uri("git://github.com/ornicar/scalex#master")
  //lazy val scalex = uri("/home/thib/scalex")

  lazy val root = Project("scalex-http", file("."), settings = buildSettings).settings(
    libraryDependencies := Seq(playmini, scalaz),
    mainClass in (Compile, run) := Some("play.core.server.NettyServer")
  ) dependsOn scalex

}
