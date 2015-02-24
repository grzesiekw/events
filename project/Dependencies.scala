import sbt._

object Version {
  val akka = "2.3.9"
  val akkaStream = "1.0-M2"
  val akkaHttp = "1.0-M2"
  val picklingVersion = "0.10.0-SNAPSHOT"

  val scalaTest = "2.2.1"

  val reactiveMongo = "0.10.5.0.akka23"

  val playJson = "2.4.0-M2"

  val webJars = "2.3.0-2"
  val bootstrap = "3.3.1"
  val angularJs = "1.3.8"
}

object Library {
  val akkaActor = "com.typesafe.akka" %% "akka-actor" % Version.akka
  val akkaStream = "com.typesafe.akka" %% "akka-stream-experimental" % Version.akkaStream
  val akkaHttp = "com.typesafe.akka" %% "akka-http-experimental" % Version.akkaHttp
  val akkaHttpCore = "com.typesafe.akka" %% "akka-http-core-experimental" % Version.akkaHttp
  val akkaHttpSprayJson = "com.typesafe.akka" %% "akka-http-spray-json-experimental" % Version.akkaHttp

  val scalaPickling = "org.scala-lang.modules" %% "scala-pickling" % Version.picklingVersion
  val playJson = "com.typesafe.play" %% "play-json" % Version.playJson

  val scalaTest = "org.scalatest" %% "scalatest" % Version.scalaTest % "test"

  val reactiveMongo = "org.reactivemongo" %% "reactivemongo" % Version.reactiveMongo

  val webJars = "org.webjars" %% "webjars-play" % Version.webJars
  val bootstrap = "org.webjars" % "bootstrap" % Version.bootstrap
  val angularJs = "org.webjars" % "angularjs" % Version.angularJs
}

object Dependencies {
  import Library._

  val apiDependencies = Seq(akkaActor, akkaStream, akkaHttp, akkaHttpCore, akkaHttpSprayJson, scalaTest)
  val storeDependencies = Seq(akkaActor, reactiveMongo, playJson)
  val uiDependencies = Seq(webJars, bootstrap, angularJs, playJson)

}

object Resolvers {
  val typesafe = "Typesafe Releases" at "http://repo.typesafe.com/typesafe/releases/"
  val scalaz = "Scalaz Bintray Repo" at "http://dl.bintray.com/scalaz/releases"
  val sonatypeSnapshots = Resolver.sonatypeRepo("snapshots")
}