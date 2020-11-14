/*
 * Copyright (c) 2020. StulSoft
 */

package com.stulsoft.akka.stream.scala.seq

import akka.Done
import akka.actor.ActorSystem
import akka.stream.Materializer
import akka.stream.scaladsl.{Sink, Source}
import com.typesafe.scalalogging.StrictLogging

import scala.concurrent.{ExecutionContextExecutor, Future, Promise}
import scala.util.{Failure, Success}

/**
 * @author Yuriy Stul
 */
object SeqEx1 extends App with StrictLogging {
  logger.info("==>main")
  val system = ActorSystem.create("SourceRangeWithFlow")
  val materializer: Materializer = Materializer.createMaterializer(system)
  implicit val ec: ExecutionContextExecutor = system.dispatcher

  seqEx1(materializer, ec)
    .map(_ => seqEx2(materializer, ec))
    .map(_ => seqEx3(materializer, ec))
    .onComplete(_ => {
      system.terminate()
      logger.info("<==main")
    })

  def seqEx1(implicit materializer: Materializer, ec: ExecutionContextExecutor): Future[Done] = {
    logger.info("==>seqEx1")
    val promise = Promise[Done]()
    val source = Source[Int](0 to 10)
    val sink = Sink.seq[Int]

    source.runWith(sink).onComplete {
      case Success(seq) =>
        logger.info("seq={}", seq)
        promise.success(Done)
      case Failure(exception) =>
        logger.error(exception.getMessage)
        promise.failure(exception)
    }
    promise.future
  }

  def seqEx2(implicit materializer: Materializer, ec: ExecutionContextExecutor): Future[Done] = {
    logger.info("==>seqEx2")
    val promise = Promise[Done]()
    val source = Source[Int](0 to 10)
    val sink = Sink.seq[Int]

    source
      .take(5)
      .runWith(sink)
      .onComplete {
        case Success(seq) =>
          logger.info("seq={}", seq)
          promise.success(Done)
        case Failure(exception) =>
          logger.error(exception.getMessage)
          promise.failure(exception)
      }
    promise.future
  }

  def seqEx3(implicit materializer: Materializer, ec: ExecutionContextExecutor): Future[Done] = {
    logger.info("==>seqEx3")
    val promise = Promise[Done]()
    val source = Source[Int](0 to 10)
    val sink = Sink.seq[Int]

    source
      .limit(6)
      .runWith(sink)
      .onComplete {
        case Success(seq) =>
          logger.info("seq={}", seq)
          promise.success(Done)
        case Failure(exception) =>
          logger.error(exception.getMessage)
          promise.failure(exception)
      }
    promise.future
  }
}
