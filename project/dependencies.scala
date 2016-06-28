import sbt._

object Dependencies {

  private val akkaVersion = "2.4.6"

  private val akkaActor = "com.typesafe.akka" %% "akka-actor" % akkaVersion
  private val akkaStream = "com.typesafe.akka" %% "akka-stream" % akkaVersion
  private val akkaHttpCore = "com.typesafe.akka" %% "akka-http-core" % akkaVersion
  private val akkaHttpExp = "com.typesafe.akka" %% "akka-http-experimental" % akkaVersion
  private val akkaHttpJson = "com.typesafe.akka" %% "akka-http-spray-json-experimental" % akkaVersion
  private val akkaHttpTest = "com.typesafe.akka" %% "akka-http-testkit" % akkaVersion
  private val scalatest = "org.scalatest" %% "scalatest" % "2.2.6"

  val appDependencies = Seq(
    akkaActor,
    akkaStream,
    akkaHttpCore,
    akkaHttpExp,
    akkaHttpJson,
    scalatest % "test"
  )

}
