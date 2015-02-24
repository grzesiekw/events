import sbt._
import Keys._

object Common {

  val commonSettings = Seq(
    organization := "com.gw",
    version := "0.1",
    scalaVersion := "2.11.5",
    ivyScala := ivyScala.value map { _.copy(overrideScalaVersion = true) }
  )

  def baseProject(name: String, fileName: String): Project = Project(name, file(fileName)).settings(commonSettings: _*)
  def baseProject(name: String): Project = baseProject(name, name)
}