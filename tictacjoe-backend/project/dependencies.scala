import sbt._

object Dependencies {

  val scalatest = "org.scalatest" %% "scalatest" % "2.2.6"

  val appDependencies = Seq(
    scalatest % "test"
  )

}
