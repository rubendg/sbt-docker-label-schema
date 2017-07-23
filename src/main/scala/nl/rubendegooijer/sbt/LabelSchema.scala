/*
 * Copyright (c) 2017 nl.rubendegooijer.sbt
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of
 * this software and associated documentation files (the "Software"), to deal in
 * the Software without restriction, including without limitation the rights to
 * use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of
 * the Software, and to permit persons to whom the Software is furnished to do so,
 * subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS
 * FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR
 * COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER
 * IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN
 * CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package nl.rubendegooijer.sbt

import java.time.Instant

import java.net.URL

final case class LabelSchema(buildDate: Option[Instant] = None,
                             name: Option[String] = None,
                             description: Option[String] = None,
                             usage: Option[String] = None,
                             url: Option[URL] = None,
                             vcsUrl: Option[URL] = None,
                             vcsRef: Option[String] = None,
                             vendor: Option[String] = None,
                             version: Option[String] = None,
                             schemaVersion: String = "1.0",
                             docker: Option[Docker] = None)

final case class DockerParam(name: String, `type`: String, description: String)

final case class Docker(cmd: Option[DockerCmd] = None, params: Option[Seq[DockerParam]] = None)

final case class DockerCmd(devel: Option[String] = None,
                           test: Option[String] = None,
                           debug: Option[String] = None,
                           help: Option[String] = None)

private[sbt] object LabelSchema {

  private[sbt] def ns(name: String, labels: Map[String, String]): Map[String, String] =
    labels.map { case (k, v) => (s"$name.$k", v) }

  private[sbt] def toMap(labelSchema: LabelSchema): Map[String, String] = {

    def formatDockerCmd(dockerCmd: DockerCmd): Map[String, String] =
      Seq(
        dockerCmd.devel.map("devel" -> _),
        dockerCmd.test.map("test" -> _),
        dockerCmd.debug.map("debug" -> _),
        dockerCmd.help.map("help" -> _)
      ).flatMap(_.toList).toMap

    def formatDockerParams(params: Seq[DockerParam]): String =
      params
        .map { param =>
          s"${param.name}=${param.`type`} ${param.description}"
        }
        .mkString(",")

    def formatDocker(docker: Docker): Map[String, String] = {
      val cmdFormatted = docker.cmd
        .map(cmd => ns("cmd", formatDockerCmd(cmd)))
        .getOrElse(Map.empty[String, String])

      Seq(docker.params.map("params" -> formatDockerParams(_))).flatMap(_.toList) ++ cmdFormatted
    }.toMap

    def formatLabelSchema(labelSchema: LabelSchema): Map[String, String] = {
      import labelSchema._

      val dockerFormatted = docker
        .map(d => ns("docker", formatDocker(d)))
        .getOrElse(Map.empty[String, String])

      ns(
        "org.label-schema", {
          Seq(
            buildDate.map("build-date" -> _.toString),
            name.map("name" -> _),
            description.map("description" -> _),
            usage.map("usage" -> _),
            url.map("url" -> _.toExternalForm),
            vcsUrl.map("vcs-url" -> _.toExternalForm),
            vcsRef.map("vcs-ref" -> _),
            vendor.map("vendor" -> _),
            version.map("version" -> _),
            Some("schema-version" -> schemaVersion)
          ).flatMap(_.toList) ++ dockerFormatted
        }.toMap
      )
    }

    formatLabelSchema(labelSchema)
  }
}
