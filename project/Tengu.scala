import sbt._
import Keys._

object Tengu extends Build {

  lazy val root = Project(
    id = "Tengu",
    base = file("."),
    settings = Project.defaultSettings ++ Seq(
      name := "Tengu",
      organization := "razon",
      scalaVersion := "2.11.1",
      resolvers += "Typesafe Repository" at "http://repo.typesafe.com/typesafe/releases/",
      fork in run := true,
      libraryDependencies ++= Seq(
        "org.scalatest" % "scalatest_2.11" % "2.2.0" % "test",
        "com.typesafe.akka" % "akka-actor" % "2.0.1",
        "org.controlsfx" % "controlsfx" % "8.0.6"
      ),
      initialCommands := "import tengu._"
    )
  )
}
