/*
 * Copyright (c) 2020. StulSoft
 */

package com.stulsoft.akka.stream.scala.custom.source

import akka.NotUsed
import akka.actor.ActorSystem
import akka.stream.{Graph, Materializer, SourceShape}
import akka.stream.scaladsl.Source

import scala.concurrent.ExecutionContextExecutor

/**
 * @author Yuriy Stul
 */
object NumbersSourceRunner extends App {
  val system = ActorSystem.create("NumbersSourceRunner")
  implicit val materializer: Materializer = Materializer.createMaterializer(system)

  // A GraphStage is a proper Graph, just like what GraphDSL.create would return
  val sourceGraph: Graph[SourceShape[Int], NotUsed] = new NumbersSource


  // Create a Source from the Graph to access the DSL
  val source: Source[Int, NotUsed] = Source.fromGraph(sourceGraph)

  implicit val ec: ExecutionContextExecutor = system.dispatcher
  source
    .take(10)
    .runForeach(println)
    .onComplete(_ => system.terminate())
}
