import sbt._
import sbt.Keys._
import Dependencies._

object AppBuilder extends Build {

  val appName = "tictactoe"

  val appSettings = Seq(
    name := appName,
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

  lazy val app = Project(appName, file("."))
    .settings(appSettings : _*)
    .settings(libraryDependencies ++= appDependencies)

}
