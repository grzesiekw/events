name := "events-ui"

maintainer in Docker := "grzesiekw <g.wilkowicz@gmail.com>"
dockerBaseImage := "dockerfile/java:oracle-java8"
dockerExposedPorts in Docker := Seq(9000)
