/*
 * Copyright (c) 2019. Yuriy Stul
 */

package com.stulsoft.akka.stream.scala.first.steps

import akka.actor.ActorSystem
import akka.stream.ActorMaterializer
import akka.stream.scaladsl.{Flow, Sink, Source}
import com.typesafe.scalalogging.LazyLogging

import scala.concurrent.ExecutionContextExecutor

/** Simple flow: source -> flow -> sink
  *
  * @author Yuriy Stul
  */
object SourceRangeWithFlow extends App with LazyLogging {
  logger.info("==>main")
  val system = ActorSystem.create("SourceRangeWithFlow")
  implicit val materializer: ActorMaterializer = ActorMaterializer.create(system)
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
