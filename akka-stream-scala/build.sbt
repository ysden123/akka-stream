import sbt.Keys.libraryDependencies

lazy val akkaVersion = "2.5.23"
lazy val scalaLoggingVersion = "3.9.2"
lazy val logbackClassicVersion = "1.2.3"

lazy val commonSettings = Seq(
  organization := "com.stulsoft",
  version := "1.0.1",
  javacOptions ++= Seq("-source", "11"),
  scalaVersion := "2.13.0",
  scalacOptions ++= Seq(
    "-feature",
    "-deprecation",
    "-language:implicitConversions",
    "-language:postfixOps"),
  libraryDependencies ++= Seq(
    "com.typesafe.akka" %% "akka-actor" % akkaVersion,
    "com.typesafe.akka" %% "akka-stream" % akkaVersion,
    "com.typesafe.akka" %% "akka-slf4j" % akkaVersion,
    "com.typesafe.scala-logging" %% "scala-logging" % scalaLoggingVersion,
    "ch.qos.logback" % "logback-classic" % logbackClassicVersion,
    "com.typesafe.akka" %% "akka-testkit" % akkaVersion
  )
)

lazy val akkaStreamScala = (project in file("."))
  .settings(commonSettings: _*)
  .settings(
    name := "akka-stream-scala"
  )

parallelExecution in Test := false
