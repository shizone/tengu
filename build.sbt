import AssemblyKeys._

import scala.scalajs.sbtplugin.ScalaJSPlugin._

import ScalaJSKeys._

val scalajsOutputDir = Def.settingKey[File]("directory for javascript files output by scalajs")

lazy val fx = Project(
  id = "tengu-fx",
  base = file("tengu-fx"),
  settings = Project.defaultSettings ++ assemblySettings ++ Seq(
    scalajsOutputDir := (crossTarget in Compile).value / "classes" / "js",
    compile in Compile <<= (compile in Compile) dependsOn (fastOptJS in (js, Compile)),
    assembly <<= assembly dependsOn (fullOptJS in (js, Compile))
  ) ++ (
    Seq(packageExternalDepsJS, packageInternalDepsJS, packageExportedProductsJS, packageLauncher, fastOptJS, fullOptJS) map { packageJSKey =>
      crossTarget in (js, Compile, packageJSKey) := scalajsOutputDir.value
    }
  )
) dependsOn(js)

lazy val js = Project(
  id = "tengu-js",
  base = file("tengu-js")
)

mainClass in assembly := Some("tengu.Tengu")
