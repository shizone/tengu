import sbt._
import Keys._

object Tengu extends Build {

  lazy val root = Project(
    id = "Tengu",
    base = file("."),
      dependencies = picture_show,
    settings = Project.defaultSettings ++ Seq(
      name := "Tengu",
      organization := "razon",
      scalaVersion := "2.9.2",
      resolvers += "Typesafe Repository" at "http://repo.typesafe.com/typesafe/releases/",
      fork in run := true,
      libraryDependencies ++= Seq(
        "org.scalatest" %% "scalatest" % "1.9.1",
        "com.typesafe.akka" % "akka-actor" % "2.0.1"
      ),
      initialCommands := "import tengu._"
    )
  )

  lazy val picture_show =
    Seq(
      ClasspathDependency(ProjectRef(uri("https://github.com/shizone/picture-show.git"), "pictureshow-core"), None),
      ClasspathDependency(ProjectRef(uri("https://github.com/shizone/picture-show.git"), "pictureshow-server"), None),
      ClasspathDependency(ProjectRef(uri("https://github.com/shizone/picture-show.git"), "pictureshow-offline"), None)
    )
}