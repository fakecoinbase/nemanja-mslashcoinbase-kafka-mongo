name := "mongo-consumer"

version := "0.1"

scalaVersion := "2.13.2"

libraryDependencies ++= Seq(
  "com.typesafe" % "config" % "1.4.0",
  "ch.qos.logback" % "logback-classic" % "1.2.3",
  "com.typesafe.scala-logging" %% "scala-logging" % "3.9.2",
  "org.apache.kafka" %% "kafka" % "2.5.0",
  "org.mongodb.scala" %% "mongo-scala-driver" % "2.9.0"
)
