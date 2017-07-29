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

import sbt.Process
import java.net.URL

import scala.util.Try

private object GitNative extends VcsSupport {
  override def headCommit: Try[String] =
    Try(Process("git rev-parse --verify HEAD").!!)
      .flatMap(result => Try(result.split("\n").head))

  override def remoteUrl: Try[String] =
    Try(Process("git config --get remote.origin.url").!!)
}

object Git {
  lazy val native: Git = new Git(GitNative)

  private[sbt] def parseRemoteUrl(url: String): Option[URL] = {
    if (url.startsWith("https")) Some(url)
    else {
      val extractDomainAndRepo = "^git@([a-z.]+):(.*)".r
      url match {
        case extractDomainAndRepo(domain, repo) =>
          Some(s"https://$domain/$repo")
        case _ => None
      }
    }
  }.flatMap(url => Try(new URL(url)).toOption)
}

class Git(val vcsSupport: VcsSupport) {
  import Git._

  def remoteUrl: Option[URL] =
    vcsSupport.remoteUrl.toOption.flatMap(parseRemoteUrl)

  def headCommit: Option[String] = vcsSupport.headCommit.toOption
}
