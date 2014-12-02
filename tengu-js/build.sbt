scalaJSSettings

name := "Tengu-js"

organization := "razon"

scalaVersion := "2.11.2"

ScalaJSKeys.persistLauncher := true

ScalaJSKeys.persistLauncher in Test := false

libraryDependencies ++= Seq(
  "org.scala-lang.modules.scalajs" %%% "scalajs-dom" % "0.6",
  "org.scala-lang.modules.scalajs" %% "scalajs-jasmine-test-framework" % scalaJSVersion % "test"
)
