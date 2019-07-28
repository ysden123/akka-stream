/*
 * Copyright (c) 2019. Yuriy Stul
 */

package com.stulsoft.akka.stream.scala.basics

import java.nio.file.Paths

import akka.actor.ActorSystem
import akka.stream.ActorMaterializer
import akka.stream.scaladsl.{FileIO, Framing}
import akka.util.ByteString

import scala.concurrent.ExecutionContextExecutor

/** Reads from a file with new lines (Windows)
 *
 * @author Yuriy Stul
 */
object FramingEx3 extends App {
  println("==>FramingEx3")
  implicit val system: ActorSystem = ActorSystem("FramingEx3")
  implicit val dispatcher: ExecutionContextExecutor = system.dispatcher
  implicit val materializer: ActorMaterializer = ActorMaterializer()


  FileIO.fromPath(Paths.get("src/main/resources/testInputFile2.txt"))
    .via(Framing.delimiter(ByteString("."), Int.MaxValue))
    .map(_.utf8String)
    .map(_.replace("\n", ""))
    .runForeach(println)
    .onComplete(_ => system.terminate())
}
