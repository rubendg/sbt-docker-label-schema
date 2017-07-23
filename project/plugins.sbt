addSbtPlugin("com.geirsson" % "sbt-scalafmt" % "0.6.6")
addSbtPlugin("de.heikoseeberger" % "sbt-header" % "2.0.0")
addSbtPlugin("org.wartremover" % "sbt-wartremover" % "2.1.1")
addSbtPlugin("com.typesafe.sbt" % "sbt-git" % "0.9.3")
addSbtPlugin("org.foundweekends" % "sbt-bintray" % "0.5.1")
addSbtPlugin("com.timushev.sbt" % "sbt-updates" % "0.3.1")

libraryDependencies += "org.scala-sbt" % "scripted-plugin" % sbtVersion.value
