/*
 * Copyright (c) 2020. StulSoft
 */

package com.stulsoft.akka.stream.scala.filter

import akka.Done
import akka.actor.ActorSystem
import akka.stream.Materializer
import akka.stream.scaladsl.{Flow, Sink, Source}
import com.typesafe.scalalogging.StrictLogging

import scala.concurrent.{ExecutionContextExecutor, Future, Promise}

/**
 * @author Yuriy Stul
 */
object FlowFilterEx1 extends App with StrictLogging {
  logger.info("==>main")
  val system = ActorSystem.create("SourceRangeWithFlow")
  val materializer: Materializer = Materializer.createMaterializer(system)
  implicit val ec: ExecutionContextExecutor = system.dispatcher

  graph1(materializer, ec)
    .map(_ => graph2(materializer, ec))
    .onComplete(_ => {
      system.terminate()
      logger.info("<==main")
    })(ec)

  /**
   * Graph: source -> filter flow -> sink
   */
  def graph1(implicit materializer: Materializer, ec: ExecutionContextExecutor): Future[Done] = {
    logger.info("==>graph1")
    val promise = Promise[Done]()
    val source = Source[Int](1 to 10)
    val flow = Flow[Int].filter(i => i <= 6)
    val sink = Sink.foreach(println)

    source
      .via(flow)
      .runWith(sink)
      .onComplete(_ => promise.success(Done))

    promise.future
  }

  /**
   * Graph: source -> filter flow -> sink
   */
  def graph2(implicit materializer: Materializer, ec: ExecutionContextExecutor): Future[Done] = {
    logger.info("==>graph2")
    val promise = Promise[Done]()
    val source = Source[Int](1 to 10)
    val flow = Flow[Int].filter(i => i > 6)
    val sink = Sink.foreach(println)

    source
      .via(flow)
      .runWith(sink)
      .onComplete(_ => promise.success(Done))

    promise.future
  }
}
