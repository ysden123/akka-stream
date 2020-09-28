/*
 * Copyright (c) 2019. Yuriy Stul
 */

package com.stulsoft.akka.stream.scala.first.steps

import akka.actor.ActorSystem
import akka.stream.Materializer
import akka.stream.scaladsl.{Flow, Sink, Source}
import com.typesafe.scalalogging.StrictLogging

import scala.concurrent.ExecutionContextExecutor

/** Simple flow: source -> flow1 -> flow2 -> sink (Sink.head)
 *
 * @author Yuriy Stul
 */
object SourceRangeCalculator extends App with StrictLogging {
  logger.info("==>main")
  val system = ActorSystem.create("SourceRangeCalculator")
  implicit val materializer: Materializer = Materializer.createMaterializer(system)
  implicit val ec: ExecutionContextExecutor = system.dispatcher

  val source = Source[Int](1 to 10)

  val flow1 = Flow[Int]
    .map(i => i + 1)

  val flow2 = Flow[Int]
    .fold(0)(_ + _)

  source
    .via(flow1)
    .via(flow2)
    .runWith(Sink.head)
    .onComplete(result => {
      if (result.isSuccess) {
        logger.info(s"Result is ${result.get}")
      }
      system.terminate()
      logger.info("<==main")
    })
}
