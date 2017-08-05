# sbt-docker-label-schema

[![Build Status](https://travis-ci.org/rubendg/sbt-docker-label-schema.svg?branch=master)](https://travis-ci.org/rubendg/sbt-docker-label-schema)

sbt-docker-label-schema is a [sbt](http://www.scala-sbt.org/) plugin for applying [label-schema](http://label-schema.org/rc1/) labels to
your Docker images created by the [sbt-native-packager](http://www.scala-sbt.org/sbt-native-packager/) Docker packaging format.

## Getting Started

In order to add the sbt-docker-label-schema plugin to your build, add the following line to `project/plugins.sbt`:

```scala
addSbtPlugin("nl.rubendegooijer" % "sbt-docker-label-schema" % "")
```

The plugin is automatically enabled if it finds that sbt-native-packager's `DockerPlugin` is enabled.

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

| label-schema                 | source                                                         |
|------------------------------|----------------------------------------------------------------|
| org.label-schema.name        | sbt.Keys.name                                                  |
| org.label-schema.description | sbt.Keys.description                                           |
| org.label-schema.version     | sbt.Keys.version                                               |
| org.label-schema.url         | sbt.Keys.apiUrl or sbt.Keys.projectInfo.organizationHomepage   |
| org.label-schema.vendor      | sbt.Keys.organizationName                                      |
| org.label-schema.vcs-url     | sbt.Keys.scmInfo.browserUrl or git remote url                  | 
| org.label-schema.vcs-ref     | git commit head                                                |

## Requirements

- Scala 2.10.6
- sbt 0.13.15 or higher
- sbt-native-packager 1.2.0 or higher

## Contribution Policy

Contributions via GitHub pull requests are gladly accepted from their original author.
Along with any pull requests, please state that the contribution is your original work 
and that you license the work to the project under the project's open source license. 
Whether or not you state this explicitly, by submitting any copyrighted material via 
pull request, email, or other means you agree to license the material under the 
project's open source license and warrant that you have the legal authority to do so.

# License 

This code is open source software licensed under the [MIT License](https://opensource.org/licenses/MIT).
