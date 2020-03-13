import sbt._

object Dependencies {

  object Versions {

    object scalatest {
      val core = "3.1.1"
    }
    object tapir {
      val core = "0.12.23"
    }

    object circe {
      val core = "0.13.0"
    }

  }

  object Libraries {

    object circe {
      val all = Seq("io.circe" %% "circe-core",
      "io.circe" %% "circe-generic",
      "io.circe" %% "circe-parser").map(_ % Versions.circe.core)
    }
    object scalatest {
      val core =  "org.scalatest" %% "scalatest" % Versions.scalatest.core % "test"
    }

    object tapir {
      val core = "com.softwaremill.sttp.tapir" %% "tapir-core" % Versions.tapir.core
      val akka = "com.softwaremill.sttp.tapir" %% "tapir-akka-http-server" % Versions.tapir.core
      val circe = "com.softwaremill.sttp.tapir" %% "tapir-json-circe" % Versions.tapir.core

      val docs = Seq(
        "com.softwaremill.sttp.tapir" %% "tapir-openapi-docs",
        "com.softwaremill.sttp.tapir" %% "tapir-openapi-circe-yaml",
        "com.softwaremill.sttp.tapir" %% "tapir-swagger-ui-akka-http"
      ).map(_ % Versions.tapir.core)
    }
  }

  object Modules {
    import Libraries._
    val endpoints = Seq(scalatest.core, tapir.core, tapir.circe) ++ circe.all

    val service = Seq(tapir.akka) ++ tapir.docs

  }

}
