import sbt.Keys.{organization, sbtPlugin, scalacOptions}

lazy val repo = "https://github.com/rubendg/sbt-docker-label-schema"

lazy val buildSettings =
  Seq(
    name := "sbt-docker-label-schema",
    organization := "nl.rubendegooijer.sbt",
    organizationName := "Ruben de Gooijer",
    description :=
      """
        |sbt-docker-label-schema is an sbt plugin for applying label-schema labels to
        |your Docker images created by the sbt-native-packager Docker packaging format.
      """.stripMargin,
    homepage := Some(url(repo)),
    startYear := Some(2017),
    licenses += ("MIT", new URL("https://opensource.org/licenses/MIT")),
    scalaVersion in Global := "2.12.4",
    scalacOptions in ThisBuild ++= Seq(
      "-deprecation",
      "-encoding",
      "UTF-8",
      "-feature",
      "-unchecked",
      "-Xfatal-warnings",
      "-Xlint",
      "-Yno-adapted-args",
      "-Ywarn-dead-code",
      "-Ywarn-numeric-widen",
      "-Ywarn-value-discard",
      "-Xfuture"
    ),
    crossSbtVersions := Vector("0.13.16", "1.1.1"),
    git.useGitDescribe := true,
    sbtPlugin := true,
    onLoad in Global := (onLoad in Global).value.andThen(st => {
      Project.runTask(dependencyUpdates, st)
      st
    })
  )

lazy val bintraySettings = Seq(
  bintrayReleaseOnPublish := false,
  bintrayPackageLabels := Seq("sbt", "docker", "label-schema", "labels", "metadata"),
  bintrayVcsUrl := Some(repo),
  publishMavenStyle := false,
  pomIncludeRepository := { _ ⇒
    false
  }
)

addSbtPlugin("com.typesafe.sbt" %% "sbt-native-packager" % "1.3.3" % "provided")

val dockerLabelSchemaPlugin = project
  .in(file("."))
  .enablePlugins(GitVersioning, GitBranchPrompt, AutomateHeaderPlugin)
  .settings(buildSettings ++ bintraySettings)
  .settings(
    scalafmtOnCompile in ThisBuild := true,
    scalafmtTestOnCompile in ThisBuild := true,
    scalafmtFailTest in ThisBuild := false,
    publishMavenStyle := false,
    scriptedBufferLog := false,
    scriptedLaunchOpts ++= Seq(
      "-Xmx1024M",
      "-Dnative-packager-version=" + {
        sbtVersion.value match {
          case v if v.startsWith("1.") => "1.3.3"
          case _ => "1.2.0"
        }
      },
      s"-Dproject.version=${version.value}"
    ),
    libraryDependencies ++= Seq("org.scalatest" %% "scalatest" % "3.0.5" % Test)
  )
