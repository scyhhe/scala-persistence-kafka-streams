import com.firstbird.underpin.UnderpinVersionsPlugin.Keys._
import sbt._

object Dependencies {

 val api = Def.setting {
    Seq(
      underpinTapir,
      "com.softwaremill.sttp.tapir" %% "tapir-json-play"          % tapirVersion.value,
      "com.softwaremill.sttp.tapir" %% "tapir-openapi-docs"       % tapirVersion.value,
      "com.softwaremill.sttp.tapir" %% "tapir-openapi-circe-yaml" % tapirVersion.value
    )
  }

  val docs = Def.setting {
    Seq("org.webjars" % "swagger-ui" % "3.25.1")
  }
}
