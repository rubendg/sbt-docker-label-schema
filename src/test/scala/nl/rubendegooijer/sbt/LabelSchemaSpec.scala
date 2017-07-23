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

import java.net.URL
import java.time.Instant

import org.scalatest.{Matchers, WordSpec}

final class LabelSchemaSpec extends WordSpec with Matchers {

  "LabelSchema" when {

    import LabelSchema._

    "constructed without any labels" should {

      "only specify the schema-version" in {
        LabelSchema().schemaVersion shouldBe "1.0"
      }

    }

    "marshalled as docker labels" should {

      "conform to the schema for the minimal case" in {
        toMap(LabelSchema()) shouldBe Map("org.label-schema.schema-version" -> "1.0")
      }

      "conform to the schema for the maximal case" in {

        val fullBlownExample = LabelSchema(
          buildDate = Some(Instant.parse("2007-12-03T10:15:30.00Z")),
          name = Some("example"),
          description = Some("some example description"),
          usage = Some("description example usage"),
          url = Some(new URL("https://example.com")),
          vcsUrl = Some(new URL("https://repo.example.com")),
          vcsRef = Some("some sha"),
          vendor = Some("example corp"),
          version = Some("1.0"),
          docker = Some(
            Docker(
              cmd = Some(
                DockerCmd(
                  devel = Some("run debug"),
                  test = Some("run test"),
                  debug = Some("run debug"),
                  help = Some("run help")
                )
              ),
              params = Some(Seq(DockerParam("X", "integer", "x desc"), DockerParam("Y", "string", "y desc")))
            )
          )
        )

        toMap(fullBlownExample) shouldBe Map(
          "org.label-schema.build-date" -> "2007-12-03T10:15:30Z",
          "org.label-schema.name" -> "example",
          "org.label-schema.description" -> "some example description",
          "org.label-schema.usage" -> "description example usage",
          "org.label-schema.url" -> "https://example.com",
          "org.label-schema.vcs-url" -> "https://repo.example.com",
          "org.label-schema.vcs-ref" -> "some sha",
          "org.label-schema.vendor" -> "example corp",
          "org.label-schema.version" -> "1.0",
          "org.label-schema.schema-version" -> "1.0",
          "org.label-schema.docker.params" -> "X=integer x desc,Y=string y desc",
          "org.label-schema.docker.cmd.devel" -> "run debug",
          "org.label-schema.docker.cmd.test" -> "run test",
          "org.label-schema.docker.cmd.debug" -> "run debug",
          "org.label-schema.docker.cmd.help" -> "run help"
        )
      }

    }

  }
}
