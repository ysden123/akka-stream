import sbt.Keys.{javacOptions, scalacOptions}

ThisBuild / scalaVersion := "2.13.4"
ThisBuild / organization := "com.stulsoft"
ThisBuild / version := "1.0.1"

lazy val loggingVersion = "2.13.3"
lazy val akkaHttpVersion = "10.2.1"
lazy val akkaVersion = "2.6.10"
lazy val json4sVersion = "3.6.10"
lazy val scalatestVersion = "3.2.2"

lazy val app = (project in file("."))
  .settings(
    name := "back-pressure",
    libraryDependencies += "com.stulsoft" %% "common" % "1.0.2",
    libraryDependencies += "com.typesafe.akka" %% "akka-stream" % akkaVersion,
    libraryDependencies += "com.typesafe.akka" %% "akka-actor-typed" % akkaVersion,
    libraryDependencies += "com.typesafe.scala-logging" %% "scala-logging" % "3.9.2",
    libraryDependencies += "org.apache.logging.log4j" % "log4j-api" % loggingVersion,
    libraryDependencies += "org.apache.logging.log4j" % "log4j-core" % loggingVersion,
    libraryDependencies += "org.apache.logging.log4j" % "log4j-slf4j-impl" % loggingVersion,
    libraryDependencies += "org.scalactic" %% "scalactic" % scalatestVersion,
    libraryDependencies += "org.scalatest" %% "scalatest" % scalatestVersion % Test,
    libraryDependencies += "com.typesafe.akka" %% "akka-stream-testkit" % akkaVersion % Test,
    javacOptions ++= Seq("-source", "11"),
    scalacOptions ++= Seq(
      "-feature",
      "-deprecation",
      "-language:implicitConversions",
      "-language:postfixOps")
  )