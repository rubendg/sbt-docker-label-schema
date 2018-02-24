import sbt.Keys._

import java.time.Instant

version := "1.0"
name := "cookie-delivery-service"
organizationName := "The Cookie Company"
description := "A service that allows you to order cookies"
apiURL := Some(new URL("http://www.cookiecompany.com/delivery"))

dockerLabelSchema in Docker := (dockerLabelSchema in Docker).value
  .copy(buildDate = Some(Instant.parse("2017-07-23T12:16:28.634Z")))

enablePlugins(JavaAppPackaging)

TaskKey[Unit]("checkIfLabelsAreApplied") := {
  val process = scala.sys.process.Process(
    "docker",
    Seq(
      "inspect",
      "--format={{range $k, $v := .Config.Labels}}{{$k}}={{$v}} {{end}}",
      s"${name.value}:${version.value}"
    )
  )

  val expectedOutput = Seq(
    "org.label-schema.build-date=2017-07-23T12:16:28.634Z",
    s"org.label-schema.description=${description.value}",
    s"org.label-schema.name=${name.value}",
    "org.label-schema.schema-version=1.0",
    s"org.label-schema.url=${apiURL.value.get}",
    s"org.label-schema.vendor=${organizationName.value}",
    s"org.label-schema.version=${version.value}"
  ).mkString(" ")

  val out = process.!!.trim().split("\n").head

  assert(out == expectedOutput, s"$out not equal to $expectedOutput")

  ()
}
