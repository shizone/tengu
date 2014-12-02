name := "Tengu-FX"

organization := "razon"

scalaVersion := "2.11.2"

resolvers += "Typesafe Repository" at "http://repo.typesafe.com/typesafe/releases/"

libraryDependencies ++= Seq(
  "org.scalatest" % "scalatest_2.11" % "2.2.0" % "test",
  "com.typesafe.akka" % "akka-actor" % "2.0.1",
  "org.controlsfx" % "controlsfx" % "8.0.6"
)
