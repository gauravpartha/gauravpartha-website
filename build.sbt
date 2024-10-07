val scala3Version = "3.5.1"

lazy val root = project
  .in(file("."))
  .settings(
    name := "gauravp-website",
    version := "0.1.0-SNAPSHOT",

    scalaVersion := scala3Version,

    libraryDependencies ++= Seq(
      "com.lihaoyi" %% "cask" % "0.9.4", 
      "com.lihaoyi" %% "scalatags" % "0.13.1",
      "com.lihaoyi" %% "os-lib" % "0.10.7",
      //"com.lihaoyi" %% "ujson" % "4.0.2",
      "org.commonmark" % "commonmark" % "0.23.0"
     // "ch.qos.logback" % "logback-classic" % "1.2.10",
     // "com.typesafe.scala-logging" %% "scala-logging" % "3.9.4",
    )
  )
