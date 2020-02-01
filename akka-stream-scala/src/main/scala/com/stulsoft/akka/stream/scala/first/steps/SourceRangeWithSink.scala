/*
 * Copyright (c) 2019. Yuriy Stul
 */

package com.stulsoft.akka.stream.scala.first.steps

import akka.actor.ActorSystem
import akka.stream.Materializer
import akka.stream.scaladsl.{Sink, Source}
import com.typesafe.scalalogging.LazyLogging

import scala.concurrent.ExecutionContextExecutor

/** Simple flow: source -> sink
 *
 * @author Yuriy Stul
 */
object SourceRangeWithSink extends App with LazyLogging {
  logger.info("==>main")
  val system = ActorSystem.create("SourceRangeWithSink")
  implicit val materializer: Materializer = Materializer.createMaterializer(system)
  implicit val ec: ExecutionContextExecutor = system.dispatcher
  val source = Source[Int](1 to 10)
  val sink = Sink.foreach(println)

  source
    .runWith(sink)
    .onComplete(_ => {
      logger.info("<==main")
      system.terminate
    })

}
