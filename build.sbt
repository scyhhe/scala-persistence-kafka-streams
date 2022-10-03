ThisBuild / organization := "com.firstbird"
ThisBuild / scalaVersion := "2.13.8"

lazy val root = project
  .in(file("."))
  .settings(
    name := "scafka"
  )
  .aggregate(events, server, consumer)

lazy val events = project
  .in(file("events"))
  .settings(
    name := "scafka-events",
    libraryDependencies ++= Dependencies.events.value
  )

lazy val server = project
  .in(file("server"))
  .settings(
    name := "scafka-server",
    libraryDependencies ++= Dependencies.server.value
  )
  .dependsOn(events)

lazy val consumer = project
  .in(file("consumer"))
  .settings(
    name := "scafka-consumer",
    libraryDependencies ++= Dependencies.consumer.value,
    connectInput in run := true
  )
  .dependsOn(events)
