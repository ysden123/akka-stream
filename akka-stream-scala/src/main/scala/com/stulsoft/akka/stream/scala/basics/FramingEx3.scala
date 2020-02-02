/*
 * Copyright (c) 2019. Yuriy Stul
 */

package com.stulsoft.akka.stream.scala.basics

import akka.actor.ActorSystem
import akka.stream.scaladsl.{FileIO, Framing}
import akka.util.ByteString

import scala.concurrent.ExecutionContextExecutor
import scala.util.{Failure, Success}

/** Reads from a file with new lines (Windows)
 *
 * @author Yuriy Stul
 */
object FramingEx3 extends App {
  println("==>FramingEx3")
  implicit val system: ActorSystem = ActorSystem("FramingEx3")
  implicit val dispatcher: ExecutionContextExecutor = system.dispatcher

  val fileName = "testInputFile2.txt"
  Utils.pathFromResource(fileName) match {
    case Success(path) =>
      FileIO.fromPath(path)
        .via(Framing.delimiter(ByteString("."), Int.MaxValue))
        .map(_.utf8String)
        .map(_.replace("\n", ""))
        .runForeach(println)
        .onComplete(_ => system.terminate())
    case Failure(ex) =>
      println(s"Cannot find resource $fileName - ${ex.getMessage}")
      system.terminate()
  }
}
