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

import org.scalatest.{Matchers, WordSpec}

import scala.util.{Failure, Success, Try}

final class GitSpec extends WordSpec with Matchers {

  case class VcsSupportImpl(headCommit: Try[String], remoteUrl: Try[String]) extends VcsSupport

  val default: VcsSupportImpl =
    VcsSupportImpl(headCommit = Success("hash"), remoteUrl = Success("https://www.example.com"))

  "Git.remoteUrl" should {

    "parse the remoteUrl when present" in {
      val git = new Git(default)
      git.remoteUrl shouldBe Some(new URL("https://www.example.com"))
    }

    "return none in case parsing fails" in {
      val git = new Git(default.copy(remoteUrl = Success("garbage")))
      git.remoteUrl shouldBe None
    }

    "return none in case the remote url could not be obtained" in {
      val git =
        new Git(default.copy(remoteUrl = Failure[String](new RuntimeException("err"))))
      git.remoteUrl shouldBe None
    }

    "rewrite a git@domain:user/repo.git style remote url to a https based one" in {
      val git = new Git(default.copy(remoteUrl = Success("git@github.com:john/some-repo.git")))
      git.remoteUrl shouldBe Some(new URL("https://github.com/john/some-repo.git"))
    }
  }

  "Git.headCommit" should {
    "return the commit hash" in {
      new Git(default).headCommit shouldBe Some("hash")
    }

    "return none when the head commit should not be obtained" in {
      new Git(default.copy(headCommit = Failure[String](new RuntimeException("err")))).headCommit shouldBe None
    }
  }

}
