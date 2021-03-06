/*
 * Copyright (c) 2019. Yuriy Stul
 */

package com.stulsoft.akka.stream.scala.first.steps

import akka.actor.ActorSystem
import akka.stream.Materializer
import akka.stream.scaladsl.{Flow, Sink, Source}
import com.typesafe.scalalogging.StrictLogging

import scala.concurrent.ExecutionContextExecutor

/** Simple graph: source -> flow -> sink
 *
 * @author Yuriy Stul
 */
object SourceRangeWithFlow extends App with StrictLogging {
  logger.info("==>main")
  val system = ActorSystem.create("SourceRangeWithFlow")
  implicit val materializer: Materializer = Materializer.createMaterializer(system)
  implicit val ec: ExecutionContextExecutor = system.dispatcher
  val source = Source[Int](1 to 10)
  val flow = Flow[Int].map(i => i + 1)
  val sink = Sink.foreach(println)

  source
    .via(flow)
    .runWith(sink)
    .onComplete(_ => {
      system.terminate
      logger.info("<==main")
    })
}
