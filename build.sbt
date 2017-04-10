import NativePackagerKeys._
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
  javaWs,
  "org.postgresql" % "postgresql" % "9.3-1102-jdbc4",
  "com.typesafe.play" %% "play-mailer" % "5.0.0",
  "org.mindrot" % "jbcrypt" % "0.3m",
  "com.adrianhurt" %% "play-bootstrap" % "1.1-P25-B4",
  "org.webjars" % "bootstrap" % "3.3.7",
  "org.webjars" % "jquery" % "2.2.3"
  
)
libraryDependencies += filters


fork in run := true