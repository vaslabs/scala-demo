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
  .enablePlugins(KubeDeploymentPlugin, KubeServicePlugin, KubeIngressPlugin)
  .settings(deploymentSettings)
  .settings(ingressSettings)
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
  dynverSeparator in ThisBuild := "-",
  dynverVTagPrefix in ThisBuild := false
)

import kubeyml.deployment.plugin.Keys._
import kubeyml.deployment.{Resource, Cpu, Memory}
import kubeyml.ingress.plugin.Keys._
import kubeyml.ingress.{Host, HttpRule, ServiceMapping, Path => IngressPath}
import kubeyml.service.plugin.Keys
import kubeyml.deployment.api._
import kubeyml.ingress.api._

lazy val deploymentSettings = Seq(
  namespace in kube := "scala-demo",
  application in kube := "scala-demo-service",
  resourceLimits in kube := Resource(Cpu.fromCores(2), Memory(4096))
)

val ingressSettings = Seq(
  ingressRules in kube := List(
    HttpRule(
      Host("scala-demo.hellosoda.com"),
      List(
        IngressPath(ServiceMapping((Keys.service in kube).value.name, 8080), "/")
      )
    )
  ),
  (ingressAnnotations in kube) := Map(
    Annotate.nginxIngress(), // this adds kubernetes.io/ingress.class: nginx
    Annotate.nginxRewriteTarget("/"), //this adds nginx.ingres
  )
)