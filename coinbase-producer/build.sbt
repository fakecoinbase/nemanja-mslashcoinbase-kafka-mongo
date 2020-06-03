name := "coinbase-producer"

version := "0.1"

scalaVersion := "2.13.2"

libraryDependencies ++= Seq(
  "com.github.andyglow" %% "websocket-scala-client" % "0.3.0",
  "com.typesafe" % "config" % "1.4.0",
  "com.typesafe.scala-logging" %% "scala-logging" % "3.9.2",
  "io.circe" %% "circe-core" % "0.12.3",
  "io.circe" %% "circe-generic" % "0.12.3",
  "io.circe" %% "circe-parser" % "0.12.3",
  "org.apache.kafka" %% "kafka" % "2.5.0"
)
