# sbt-docker-label-schema

sbt-docker-label-schema is a [sbt](http://www.scala-sbt.org/) plugin for applying [label-schema](http://label-schema.org/rc1/) labels to
your Docker images created by the [sbt-native-packager](http://www.scala-sbt.org/sbt-native-packager/) Docker packaging format.

## Getting Started

In order to add the sbt-docker-label-schema plugin to your build, add the following line to `project/plugins.sbt`:

```scala
addSbtPlugin("nl.rubendegooijer" % "sbt-docker-label-schema" % "")
```

This will automatically enable the plugin if you've enabled the sbt-native-packager `DockerPlugin`.

Running

```bash
# docker image
sbt docker:publishLocal 

# inspect the image 
docker inspect --format='{{.Config.Labels}}' {image_id}
```

will by default apply a sensible mapping from already existing sbt keys to [label-schema](http://label-schema.org/rc1/) labels.
You can further add to or override this mapping by modifying the `dockerLabelSchema` setting key:

```scala
dockerLabelSchema := dockerLabelSchema.value.copy(
  usage = Some("/usr/doc/app-usage.txt")
)
```

## Contribution Policy

Contributions via GitHub pull requests are gladly accepted from their original author.
Along with any pull requests, please state that the contribution is your original work 
and that you license the work to the project under the project's open source license. 
Whether or not you state this explicitly, by submitting any copyrighted material via 
pull request, email, or other means you agree to license the material under the 
project's open source license and warrant that you have the legal authority to do so.

# License 

This code is open source software licensed under the [MIT License](https://opensource.org/licenses/MIT).
