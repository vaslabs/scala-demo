name := "scala-demo"

version := "0.1"

scalaVersion := "2.13.1"


lazy val demo = (project in file("."))
  .aggregate(endpoints, service)

lazy val endpoints =
  (project in file("endpoints"))
      .settings(
        libraryDependencies ++= Dependencies.Modules.endpoints
      )

lazy val service = (project in file("service"))
  .settings(libraryDependencies ++= Dependencies.Modules.service)
  .enablePlugins(DockerPlugin, JavaAppPackaging, AshScriptPlugin)
  .settings(dockerSettings)
  .dependsOn(endpoints)


lazy val dockerSettings = Seq(
  (packageName in Docker) := "scala-demo",
  dockerBaseImage := "openjdk:8u191-jre-alpine3.8",
  dockerRepository := Some("git.hellosoda.com:5000"),
  dockerUsername := Some("eng"),
  dockerExposedPorts := List(8080)
)