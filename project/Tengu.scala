import sbt._
import Keys._

object Tengu extends Build {

  lazy val root = Project(
    id = "Tengu",
    base = file("."),
    settings = Project.defaultSettings ++ Seq(
      name := "Tengu",
      organization := "razon",
      scalaVersion := "2.10.3",
      resolvers += "Typesafe Repository" at "http://repo.typesafe.com/typesafe/releases/",
      fork in run := true,
      libraryDependencies ++= Seq(
        "org.scalatest" %% "scalatest" % "1.9.1",
        "com.typesafe.akka" % "akka-actor" % "2.0.1",
        "org.controlsfx" % "controlsfx" % "8.0.6",
        "de.jensd" % "fontawesomefx" % "8.0.8"
      ),
      initialCommands := "import tengu._"
    )
  )
}
