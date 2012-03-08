import sbt._
import Keys._

trait Resolvers {
  val typesafe = "typesafe.com" at "http://repo.typesafe.com/typesafe/releases/"
  val iliaz = "iliaz.com" at "http://scala.iliaz.com/"
}

trait Dependencies {
  val specs2 = "org.specs2" %% "specs2" % "1.8.1"
  val playmini  = "com.typesafe" %% "play-mini" % "2.0-RC4"
}

object ScalexHttpBuild extends Build with Resolvers with Dependencies {

  val buildOrganization = "com.jirafe"
  val buildScalaVersion = "2.9.1"

  val buildSettings = List(
    Seq(
      libraryDependencies in Test ++= Seq(specs2),
      resolvers := Seq(typesafe, iliaz),
      shellPrompt := {
        (state: State) ⇒ "%s> ".format(Project.extract(state).currentProject.id)
      },
      scalacOptions := Seq("-deprecation", "-unchecked"),
      testOptions in Test += Tests.Argument("junitxml", "console"),
      exportJars := true)
  ).foldLeft(Defaults.defaultSettings)(_ ++ _)

  lazy val root = Project(id = "scalex-http", base = file("."),
    settings = buildSettings ++ Seq(
      libraryDependencies ++= Seq(playmini)
    )
  ).settings(mainClass in (Compile, run) := Some("play.core.server.NettyServer"))

}
