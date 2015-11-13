name := """first_app"""

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayScala)

scalaVersion := "2.11.1"

libraryDependencies ++= Seq(
  jdbc,
  anorm,
  cache,
  ws
)


mergeStrategy in assembly := {
  case PathList("play", "core", "server", "ServerWithStop.class") => MergeStrategy.last
  case x =>
    val oldStrategy = (mergeStrategy in assembly).value
    oldStrategy(x)
}

libraryDependencies ++= Seq(
  ws exclude("commons-logging", "commons-logging"),
  "com.softwaremill.macwire" %% "macros" % "0.7.3",
  "org.scalatestplus" %% "play" % "1.2.0" % "test",
  "org.scalamock" %% "scalamock-scalatest-support" % "3.2" % "test",
  "de.leanovate.play-mockws" %% "play-mockws" % "0.12" % "test"
)