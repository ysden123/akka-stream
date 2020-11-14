/*
 * Copyright (c) 2020. StulSoft
 */

package com.stulsoft.akka.stream.scala.operator

import akka.Done
import akka.actor.ActorSystem
import akka.stream.Materializer
import akka.stream.scaladsl.{Sink, Source}
import com.typesafe.scalalogging.StrictLogging

import scala.concurrent.{ExecutionContextExecutor, Future, Promise}

/**
 * @author Yuriy Stul
 */
object OperatorScanEx1  extends App with StrictLogging {
  logger.info("==>main")
  val system = ActorSystem.create("SourceRangeWithFlow")
  val materializer: Materializer = Materializer.createMaterializer(system)
  implicit val ec: ExecutionContextExecutor = system.dispatcher

  scanEx1(materializer, ec)
    .map( _ => foldEx1(materializer, ec))
    .onComplete(_ =>{
      logger.info("<==main")
      system.terminate()
    })

  def scanEx1(implicit materializer: Materializer, ec: ExecutionContextExecutor): Future[Done] = {
    logger.info("==>scanEx1")
    val promise = Promise[Done]()
    val source = Source[Int](0 to 10)
    val sink = Sink.foreach[Int](println)

    source
      .scan(100)((acc, i) => acc + i)
      .runWith(sink)
      .onComplete(_ => promise.success(Done))

    promise.future
  }

  def foldEx1(implicit materializer: Materializer, ec: ExecutionContextExecutor): Future[Done] = {
    logger.info("==>foldEx1")
    val promise = Promise[Done]()
    val source = Source[Int](0 to 10)
    val sink = Sink.foreach[Int](println)

    source
      .fold(100)((acc, i) => acc + i)
      .runWith(sink)
      .onComplete(_ => promise.success(Done))

    promise.future
  }
}
