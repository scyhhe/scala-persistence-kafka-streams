import com.typesafe.sbt.web.Import.WebKeys._

lazy val commonSettings = Seq(
  organization := "com.firstbird",
  scalacOptions in Test -= "-Ywarn-dead-code" // https://github.com/mockito/mockito-scala/issues/29
)

lazy val root = project
  .in(file("."))
  .settings(commonSettings)
  .settings(
    name := "jobapi",
    fbrdReleaseTasks ++= Seq(
      docs / makeSite
      // service / Docker / publishLocal //publish the produced docker image locally for further custom publishing
    )
  )
  .aggregate(api, model, events, server)
  .enablePlugins(FbrdNoPublish)

lazy val model = project
  .in(file("model"))
  .settings(commonSettings)
  .settings(
    name := "jobapi-model",
    libraryDependencies ++= Dependencies.model.value,
    crossBuilding := true
  )

lazy val api = project
  .in(file("api"))
  .settings(commonSettings)
  .settings(
    name := "jobapi-api-definition",
    libraryDependencies ++= Dependencies.api.value,
    crossBuilding := true
  )
  .dependsOn(model)

lazy val events = project
  .in(file("events"))
  .settings(commonSettings)
  .settings(
    name := "jobapi-events",
    libraryDependencies ++= Dependencies.events.value,
    crossBuilding := true
  )
  .dependsOn(model)

lazy val server = project
  .in(file("server"))
  .settings(commonSettings)
  .settings(
    name := "jobapi-server",
    libraryDependencies ++= Dependencies.server.value,
    crossBuilding := true
  )
  .dependsOn(api, events)

lazy val docs =
  project
    .in(file("docs"))
    .enablePlugins(FbrdDocsPlugin, SbtWeb)
    .settings(
      name                 := "job-api-docs",
      fbrdDocsBucketFolder := "referral-api",
      libraryDependencies ++= Dependencies.docs.value,
      Compile / paradoxProperties ++= Map(
        "snip.project.base_dir" -> (baseDirectory in ThisBuild).value.getAbsolutePath
      ),
      makeSite / mappings ++= ((api / baseDirectory).value / "target" ** "*openapi.yaml").get
        .map(file => file -> file.getName),
      makeSite                  := makeSite.dependsOn((api / Compile / run).toTask("")).value,
      makeSite / siteSubdirName := "swagger",
      addMappingsToSiteDir(
        Def.task {
          (TestAssets / assets / mappings).value.map { case (key, value) =>
            (key, value.replace("lib/swagger-ui/", ""))
          }
        },
        siteSubdirName in makeSite
      )
    )
    .dependsOn(api)
