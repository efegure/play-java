name := """play-java"""

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayJava)
lazy val myProject = (project in file("."))
  .enablePlugins(PlayJava, PlayEbean)
scalaVersion := "2.11.7"
lazy val nonEnhancedProject = (project in file("non-enhanced"))
  .disablePlugins(PlayEnhancer)
libraryDependencies ++= Seq(
  javaJdbc,
  cache,
  javaWs
)


fork in run := true