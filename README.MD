# sbt-docker-label-schema

[![Build Status](https://travis-ci.org/rubendg/sbt-docker-label-schema.svg?branch=master)](https://travis-ci.org/rubendg/sbt-docker-label-schema)
[![Download](https://api.bintray.com/packages/rubendg/sbt-plugins/sbt-docker-label-schema/images/download.svg) ](https://bintray.com/rubendg/sbt-plugins/sbt-docker-label-schema/_latestVersion)

sbt-docker-label-schema is a [sbt](http://www.scala-sbt.org/) plugin for automatically applying 
[label-schema](http://label-schema.org/rc1/) labels to Docker images created by the 
[sbt-native-packager](http://www.scala-sbt.org/sbt-native-packager/) plugin.

## Usage Instructions

In order to add the sbt-docker-label-schema plugin to your build, add the following lines to `project/plugins.sbt`:

```scala
resolvers += Resolver.bintrayIvyRepo("rubendg", "sbt-plugins")

addSbtPlugin("nl.rubendegooijer" % "sbt-docker-label-schema" % "0.1.1")
```

The plugin currently supports sbt versions `>= 0.13.16` and `1.x`, and is tested against `sbt-native-packager`
`1.2.2` and `1.3.3`. The plugin **does not** automatically include a dependency on the `sbt-native-packager` 
you'll have to do that yourself:

```scala
addSbtPlugin("com.typesafe.sbt" %% "sbt-native-packager" % "1.3.3")
```

To see the result of activating the plugin run:

```bash
# docker image
sbt docker:publishLocal 

# and inspect the image's labels
docker inspect --format='{{.Config.Labels}}' "<IMAGE ID HERE>"
```

this will by default apply a sensible mapping from already existing sbt keys to [label-schema](http://label-schema.org/rc1/) labels.
You can further add to or override this mapping by modifying the `dockerLabelSchema` setting key, e.g. like this:

```scala
dockerLabelSchema in Docker := (dockerLabelSchema in Docker).value.copy(
  usage = Some("/usr/doc/app-usage.txt")
)
```

## Default mapping

This table describes the default mapping of SBT keys or otherwise to label-schema keys.  

| source                                                         | label-schema                  |
|----------------------------------------------------------------|-------------------------------|
| sbt.Keys.name                                                  | org.label-schema.name         |
| sbt.Keys.description                                           | org.label-schema.description  |
| now                                                            | org.label-schema.build-date   |
| sbt.Keys.version                                               | org.label-schema.version      |
| sbt.Keys.apiUrl or sbt.Keys.projectInfo.organizationHomepage   | org.label-schema.url          |
| sbt.Keys.organizationName                                      | org.label-schema.vendor       |
| sbt.Keys.scmInfo.browserUrl or git remote url                  | org.label-schema.vcs-url      |
| git commit head                                                | org.label-schema.vcs-ref      |


## Contribution Policy

Contributions via GitHub pull requests are gladly accepted from their original author.
Along with any pull requests, please state that the contribution is your original work 
and that you license the work to the project under the project's open source license. 
Whether or not you state this explicitly, by submitting any copyrighted material via 
pull request, email, or other means you agree to license the material under the 
project's open source license and warrant that you have the legal authority to do so.

# License 

This code is open source software licensed under the [MIT License](https://opensource.org/licenses/MIT).
