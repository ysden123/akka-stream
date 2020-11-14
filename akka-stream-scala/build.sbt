import sbt.Keys.libraryDependencies

lazy val akkaVersion = "2.6.10"
lazy val scalaLoggingVersion = "3.9.2"
lazy val loggingVersion = "2.14.0"

lazy val commonSettings = Seq(
  organization := "com.stulsoft",
  version := "1.0.2",
  javacOptions ++= Seq("-source", "11"),
  scalaVersion := "2.13.3",
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

    "com.typesafe.scala-logging" %% "scala-logging" % scalaLoggingVersion,
    "org.apache.logging.log4j" % "log4j-api" % loggingVersion,
    "org.apache.logging.log4j" % "log4j-core" % loggingVersion,
    "org.apache.logging.log4j" % "log4j-slf4j-impl" % loggingVersion,

//    "ch.qos.logback" % "logback-classic" % logbackClassicVersion,
    "com.typesafe.akka" %% "akka-testkit" % akkaVersion
  )
)

lazy val akkaStreamScala = (project in file("."))
  .settings(commonSettings: _*)
  .settings(
    name := "akka-stream-scala"
  )

parallelExecution in Test := false
