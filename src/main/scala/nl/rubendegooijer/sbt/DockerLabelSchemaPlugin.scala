/*
 * Copyright (c) 2017 Ruben de Gooijer
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

import sbt._
import Keys._
import com.typesafe.sbt.packager.docker.{Cmd, DockerPlugin}

object DockerLabelSchemaPlugin extends AutoPlugin {

  object autoImport {
    val dockerLabelSchema: SettingKey[LabelSchema] =
      SettingKey[LabelSchema]("dockerLabelSchema", "The label-schema data to be attached as Docker labels")
  }

  import autoImport._
  import DockerPlugin.autoImport._

  override def requires: Plugins = DockerPlugin

  override def trigger: PluginTrigger = allRequirements

  private val labelSchemaAsDockerLabels: LabelSchema => Option[String] =
    LabelSchema.toMap _ andThen DockerLabel.fromMap

  @SuppressWarnings(Array("org.wartremover.warts.Any", "org.wartremover.warts.Nothing"))
  lazy val baseDockerLabelSchemaSettings: Seq[Setting[_]] = Seq(
    dockerLabelSchema := LabelSchema(
      name = Some(name.value),
      buildDate = Some(Instant.now()),
      description = Some(description.value),
      version = Some(version.value),
      url = Seq(apiURL.value, projectInfo.value.organizationHomepage).pickFirst,
      vendor = Some(organizationName.value),
      vcsUrl = Seq(scmInfo.value.map(_.browseUrl), Git.native.remoteUrl).pickFirst,
      vcsRef = Git.native.headCommit
    ),
    dockerCommands ++= labelSchemaAsDockerLabels(dockerLabelSchema.value)
      .map(labelSchemaLabels => Seq(Cmd("LABEL", labelSchemaLabels)))
      .getOrElse(Seq.empty)
  )

  @SuppressWarnings(Array("org.wartremover.warts.Any", "org.wartremover.warts.Nothing"))
  override lazy val projectSettings: Seq[Def.Setting[_]] = inConfig(Docker)(baseDockerLabelSchemaSettings)
}
