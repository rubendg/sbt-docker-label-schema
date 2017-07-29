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

import org.scalatest.{Matchers, WordSpec}
import DockerLabel._

final class DockerLabelSpec extends WordSpec with Matchers {

  "label" should {

    "quote values" in {
      label("a", "b") shouldBe """a="b""""
    }

  }

  "fromMap" should {

    "when the map is empty return no labels" in {
      fromMap(Map.empty[String, String]) shouldBe None
    }

    "when the map contains a single element do the same as label" in {
      fromMap(Map("a" -> "b")) shouldBe Some(label("a", "b"))
    }

    "separate labels by spaces" in {
      fromMap(Map("a" -> "b", "b" -> "c")) shouldBe Some("""a="b" b="c"""")
    }

    "sort labels by key" in {
      fromMap(Map("b" -> "c", "a" -> "b")) shouldBe Some("""a="b" b="c"""")
    }

  }

}
