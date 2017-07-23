import sbt.Keys.{organization, sbtPlugin, scalacOptions}
import sbt.ScriptedPlugin.scriptedBufferLog

lazy val repo = "https://github.com/rubendg/sbt-docker-label-schema"

lazy val buildSettings =
  Seq(
    name := "docker-label-schema",
    organization := "nl.rubendegooijer.sbt",
    description :=
      """
        |sbt-docker-label-schema is an sbt plugin for applying label-schema labels to
        |your Docker images created by the sbt-native-packager Docker packaging format.
      """.stripMargin,
    homepage := Some(url(repo)),
    startYear := Some(2017),
    licenses += ("MIT", new URL("https://opensource.org/licenses/MIT")),
    wartremoverErrors ++= Warts.allBut(Wart.DefaultArguments, Wart.Overloading),
    scalaVersion := "2.10.6",
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
    git.useGitDescribe := true,
    sbtPlugin := true,
    onLoad in Global := (onLoad in Global).value.andThen(st => {
      Project.runTask(dependencyUpdates, st)
      st
    })
  )

lazy val bintraySettings = Seq(
  bintrayReleaseOnPublish := false,
  bintrayPackageLabels := Seq("sbt", "docker", "label-schema", "labels"),
  bintrayVcsUrl := Some(repo),
  publishMavenStyle := false,
  pomIncludeRepository := { _ ⇒
    false
  }
)

addSbtPlugin("com.typesafe.sbt" % "sbt-native-packager" % "1.2.0")

val dockerLabelSchemaPlugin = project
  .in(file("."))
  .enablePlugins(GitVersioning, AutomateHeaderPlugin)
  .settings(buildSettings ++ bintraySettings ++ scriptedSettings)
  .settings(
    publishMavenStyle := false,
    scriptedLaunchOpts ++= Seq("-Xmx1024M", s"-Dplugin.version=${version.value}"),
    scriptedBufferLog := false,
    libraryDependencies ++= Seq("org.scalatest" %% "scalatest" % "3.0.3" % Test)
  )
