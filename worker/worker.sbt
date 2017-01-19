organization := "ru.egorodov.worker"

name := "worker"

version := "0.0.1-Worker"

scalaVersion := "2.11.8"

libraryDependencies ++= Seq(
  "com.typesafe.akka" %% "akka-actor" % "2.4.16",
  "com.typesafe.akka" %% "akka-cluster" % "2.4.16",
  "io.netty" % "netty" % "3.7.0.Final"
)

target in assembly := file("builds")
