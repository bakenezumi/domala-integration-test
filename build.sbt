lazy val root = (project in file(".")).settings(
  inThisBuild(List(
    scalaVersion := "2.12.4",
    version := "0.1.0"
  )),
  name := "domala-integration-test",
  metaMacroSettings,
  parallelExecution in Test := false,
  compile in Compile := ((compile in Compile) dependsOn (copyResources in Compile)).value,
  libraryDependencies ++= Seq(
    "com.github.domala" %% "domala" % "0.1.0-beta.9",
    "com.github.domala" %% "domala-paradise" % "0.1.0-beta.9" % Provided,
    "org.scalameta" %% "scalameta" % "1.8.0" % Provided,
    "com.typesafe" % "config" % "1.3.1" % Test,
    "org.scalatest" %% "scalatest" % "3.0.4" % Test,
    "com.h2database" % "h2" % "1.4.196" % Test,
    "mysql" % "mysql-connector-java" % "5.1.29",
    "org.postgresql" % "postgresql" % "9.4.1212" % Test
  )
)

lazy val metaMacroSettings: Seq[Def.Setting[_]] = Seq(
  addCompilerPlugin("org.scalameta" % "paradise" % "3.0.0-M10" cross CrossVersion.full),
  scalacOptions += "-Xplugin-require:macroparadise",
  scalacOptions in (Compile, console) ~= (_ filterNot (_ contains "paradise")) // macroparadise plugin doesn't work in repl yet.
)
