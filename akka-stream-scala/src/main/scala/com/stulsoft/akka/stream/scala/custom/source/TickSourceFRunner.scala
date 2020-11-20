/*
 * Copyright (c) 2020. StulSoft
 */

package com.stulsoft.akka.stream.scala.custom.source

import akka.NotUsed
import akka.actor.ActorSystem
import akka.stream.scaladsl.Source
import akka.stream.{Graph, Materializer, SourceShape}
import com.typesafe.scalalogging.StrictLogging

import scala.concurrent.ExecutionContextExecutor

/**
 * @author Yuriy Stul
 */
object TickSourceFRunner extends App with StrictLogging {
  val system = ActorSystem.create("TickSourceFRunner")
  implicit val materializer: Materializer = Materializer.createMaterializer(system)

  val sourceGraph: Graph[SourceShape[String], NotUsed] = new TickSourceF(system)

  val source: Source[String, NotUsed] = Source.fromGraph(sourceGraph)

  implicit val ec: ExecutionContextExecutor = system.dispatcher

  logger.info("Main started")
  source
    .take(10)
    .runForeach(s => logger.info(s))
    .onComplete(_ => {
      logger.info("Main finished")
      system.terminate()
    })
}
