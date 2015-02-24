import Common._
import Dependencies._
import Resolvers._

name := "events"

lazy val events = baseProject("events", ".").aggregate(eventsApi, eventsStore, eventsUi)

lazy val eventsApi = baseProject("events-api").enablePlugins(JavaAppPackaging).settings(libraryDependencies ++= apiDependencies)
  .dependsOn(eventsStore)
  .settings(resolvers += sonatypeSnapshots)

lazy val eventsStore = baseProject("events-store").settings(libraryDependencies ++= storeDependencies)
  .settings(resolvers += typesafe)

lazy val eventsUi = baseProject("events-ui").enablePlugins(PlayScala, SbtWeb).settings(libraryDependencies ++= uiDependencies)
  .dependsOn(eventsStore)
  .settings(resolvers += scalaz)
