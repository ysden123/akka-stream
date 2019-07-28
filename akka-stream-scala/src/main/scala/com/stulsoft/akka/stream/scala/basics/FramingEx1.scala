/*
 * Copyright (c) 2019. Yuriy Stul
 */

package com.stulsoft.akka.stream.scala.basics

import akka.actor.ActorSystem
import akka.stream.ActorMaterializer
import akka.stream.scaladsl.{Framing, Source}
import akka.util.ByteString
//import com.typesafe.config.ConfigFactory

import scala.concurrent.ExecutionContextExecutor

/** Reads from text
 *
 * @author Yuriy Stul
 */
object FramingEx1 extends App {

  object chunks {
    val first = ByteString("Lorem Ipsum is simply.Dummy text of the printing.And typesetting industry.")
    val second = ByteString("More text.delimited by.a period.")
  }

  implicit val system: ActorSystem = ActorSystem("FramingEx1")
  implicit val dispatcher: ExecutionContextExecutor = system.dispatcher
  implicit val materializer: ActorMaterializer = ActorMaterializer()

  Source(chunks.first :: chunks.second :: Nil)
    .via(Framing.delimiter(ByteString("."), Int.MaxValue))
    .map(_.utf8String)
    .runForeach(println)
    .onComplete(_ => system.terminate())
}
