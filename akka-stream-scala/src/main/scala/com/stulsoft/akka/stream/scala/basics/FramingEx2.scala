/*
 * Copyright (c) 2019. Yuriy Stul
 */

package com.stulsoft.akka.stream.scala.basics

package com.stulsoft.akka.stream.scala.basics

import java.io.File
import java.nio.file.Paths

import akka.actor.ActorSystem
import akka.stream.ActorMaterializer
import akka.stream.scaladsl.{FileIO, Framing, Source}
import akka.util.ByteString

import scala.concurrent.ExecutionContextExecutor

/** Reads from a file
 *
 * @author Yuriy Stul
 */
object FramingEx2 extends App {
  implicit val system: ActorSystem = ActorSystem("FramingEx2")
  implicit val dispatcher: ExecutionContextExecutor = system.dispatcher
  implicit val materializer: ActorMaterializer = ActorMaterializer()


  FileIO.fromPath(Paths.get("src/main/resources/testInputFile1.txt"))
    .via(Framing.delimiter(ByteString("."), Int.MaxValue))
    .map(_.utf8String)
    .runForeach(println)
    .onComplete(_ => system.terminate())
}
