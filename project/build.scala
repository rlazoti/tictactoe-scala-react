import sbt._
import sbt.Keys._
import Dependencies._

object AppBuilder extends Build {

  val appSettings = Seq(
    name := "tictacjoe-backend",
    organization := "io.github.rlazoti",
    version := "0.0.1-SNAPSHOT",
    scalaVersion := "2.11.8",
    scalacOptions := Seq("-deprecation", "-feature", "-unchecked", "-language:postfixOps"),

    fork in run := false,
    fork in Test := true,
    parallelExecution in Test := false,

    unmanagedSourceDirectories in Compile <<= (scalaSource in Compile)(Seq(_)),
    unmanagedSourceDirectories in Test    <<= (scalaSource in Test)(Seq(_))
  )

  lazy val app = Project("tictacjoe-backend", file("."))
    .settings(appSettings : _*)
    .settings(libraryDependencies ++= appDependencies)

}
