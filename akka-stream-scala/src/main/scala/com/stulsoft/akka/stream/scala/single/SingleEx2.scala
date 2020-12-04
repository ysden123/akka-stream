/*
 * Copyright (c) 2020. StulSoft
 */

package com.stulsoft.akka.stream.scala.single

import akka.Done
import akka.actor.ActorSystem
import akka.stream.Materializer
import akka.stream.scaladsl.{Sink, Source}
import com.typesafe.scalalogging.StrictLogging

import scala.collection.immutable
import scala.concurrent.{ExecutionContextExecutor, Future, Promise}
import scala.util.Success

/**
 * @author Yuriy Stul
 */
object SingleEx2 extends App with StrictLogging {
  logger.info("==>main")
  val system = ActorSystem.create("SingleEx2")
  implicit val materializer: Materializer = Materializer.createMaterializer(system)
  implicit val ec: ExecutionContextExecutor = system.dispatcher

  test1()
    .andThen(_ => test2())
    .onComplete(_ => system.terminate())

  def test1(): Future[Done] = {
    logger.info("==>test1")

    val promise = Promise[Done]()
    val s: Future[immutable.Seq[Int]] = Source.single(1).runWith(Sink.seq)

    s.onComplete(_ => {
      s.foreach(list => logger.info(s"Collected elements: $list")) // prints: Collected elements: List(1)
      promise.success(Done)
    })

    promise.future
  }

  def test2(): Future[Done] = {
    logger.info("==>test2")
    val promise = Promise[Done]()

    calc2()
      .onComplete(r => {
        r match {
          case Success(i: Int) =>
            logger.info(s"i: $i")
          case _ =>
        }
        promise.success(Done)
      }
      )

    promise.future
  }

  def calc2(): Future[Int] = {
    logger.info("==>calc2")

    val promise = Promise[Int]()
    val s: Future[immutable.Seq[Int]] = Source.single(1).runWith(Sink.seq)
    s.onComplete(_ => {
      s.foreach(list => promise.success(list.head))
    })
    promise.future
  }

}
