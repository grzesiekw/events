name := "events-api"

maintainer in Docker := "grzesiekw <g.wilkowicz@gmail.com>"
dockerBaseImage := "dockerfile/java:oracle-java8"
dockerExposedPorts in Docker := Seq(8080)

