import sbt._

object Dependencies {
  val events = Def.setting {
    Seq("com.typesafe.play" %% "play-json" % "2.9.3")
  }

  val server = Def.setting {
    Seq(
      "com.typesafe.play" %% "play-json"           % "2.9.3",
      "org.apache.kafka"  %% "kafka-streams-scala" % "3.1.0",
      "org.rocksdb"        % "rocksdbjni"          % "6.29.5",
      "org.slf4j"          % "slf4j-simple"        % "1.7.36"
    )
  }

  val consumer = Def.setting {
    Seq(
      "org.apache.kafka" %% "kafka-streams-scala" % "3.1.0",
      "org.typelevel"    %% "cats-effect"         % "3.3.14"
    )
  }
}
