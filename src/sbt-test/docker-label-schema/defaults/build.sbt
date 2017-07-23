import java.time.Instant

name := "docker-label-schema-plugin-defaults"

version := "1.0"

scalaVersion := "2.11.6"

dockerLabelSchema := dockerLabelSchema.value.copy(buildDate = Some(Instant.parse("2017-07-23T12:16:28.634Z")))

enablePlugins(JavaAppPackaging)

TaskKey[Unit]("checkIfLabelsAreApplied") := {
  sbt.Process
  val process = sbt.Process(
    "docker",
    Seq(
      "inspect",
      "--format={{range $k, $v := .Config.Labels}}{{$k}}={{$v}} {{end}}",
      "docker-label-schema-plugin-defaults:1.0"
    )
  )

  val expectedOutput = Seq(
    "org.label-schema.build-date=2017-07-23T12:16:28.634Z",
    "org.label-schema.description=docker-label-schema-plugin-defaults",
    "org.label-schema.name=docker-label-schema-plugin-defaults",
    "org.label-schema.schema-version=1.0",
    "org.label-schema.vendor=docker-label-schema-plugin-defaults",
    "org.label-schema.version=1.0"
  ).mkString(" ")

  val out = (process !!).trim().split("\n").head

  if (out != expectedOutput) sys.error("unexpected output: " + out)
  ()
}
