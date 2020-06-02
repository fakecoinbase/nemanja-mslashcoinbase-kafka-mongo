name := "coinbase-producer"

version := "0.1"

scalaVersion := "2.13.2"

libraryDependencies ++= Seq(
  "com.github.andyglow" %% "websocket-scala-client" % "0.3.0",
  "com.typesafe" % "config" % "1.4.0",
  "com.typesafe.scala-logging" %% "scala-logging" % "3.9.2",
  "org.json4s" %% "json4s-native" % "3.6.8",
)
