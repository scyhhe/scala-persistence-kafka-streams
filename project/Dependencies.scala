import com.firstbird.underpin.UnderpinVersionsPlugin.Keys._
import sbt._

object Dependencies {

  val api = Def.setting {
    Seq(
      underpinTapir,
      "com.softwaremill.sttp.tapir" %% "tapir-json-play"          % tapirVersion.value,
      "com.softwaremill.sttp.tapir" %% "tapir-openapi-docs"       % tapirVersion.value,
      "com.softwaremill.sttp.tapir" %% "tapir-openapi-circe-yaml" % tapirVersion.value,
      "org.gnieh"                   %% "diffson-play-json"        % "4.1.1"
    )
  }

  val model = Def.setting {
    Seq("com.typesafe.play" %% "play-json" % playJsonVersion.value)
  }

  val events = Def.setting {
    Seq("com.typesafe.play" %% "play-json" % playJsonVersion.value)
  }

  val docs = Def.setting {
    Seq("org.webjars" % "swagger-ui" % "3.25.1")
  }

  val server = Def.setting {
    Seq(
      "com.typesafe.play" %% "play-json"           % playJsonVersion.value,
      "org.apache.kafka"  %% "kafka-streams-scala" % "3.1.0",
      "org.rocksdb"        % "rocksdbjni"          % "6.29.5",
      "org.slf4j"          % "slf4j-simple"        % "1.7.36"
    )
  }

  val consumer = Def.setting {
    Seq(
      "org.apache.kafka" %% "kafka-streams-scala" % "3.1.0"
    )
  }
}
