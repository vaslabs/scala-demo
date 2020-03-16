name := "scala-demo"

version := "0.1"

scalaVersion in ThisBuild := "2.13.1"


lazy val demo = (project in file("."))
  .settings(noPublishSettings)
  .aggregate(endpoints, service)

lazy val endpoints =
  (project in file("endpoints"))
      .settings(
        libraryDependencies ++= Dependencies.Modules.endpoints
      ).settings(noPublishSettings)



lazy val service = (project in file("service"))
  .settings(libraryDependencies ++= Dependencies.Modules.service)
  .enablePlugins(DockerPlugin, JavaAppPackaging, AshScriptPlugin)
  .settings(dockerSettings)
  .settings(versioningSettings)
  .dependsOn(endpoints)


lazy val dockerSettings = Seq(
  (packageName in Docker) := "scala-demo",
  dockerBaseImage := "openjdk:8u191-jre-alpine3.8",
  dockerRepository := Some("git.hellosoda.com:5005"),
  dockerUsername := Some("eng"),
  dockerExposedPorts := List(8080)
)

lazy val noPublishSettings = Seq(
  publish / skip := true
)

lazy val versioningSettings = Seq(
  dynverSeparator in ThisBuild := "-"
)
