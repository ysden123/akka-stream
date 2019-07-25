/*
 * Copyright (c) 2019. Yuriy Stul
 */

package com.stulsoft.akka.stream.scala.basics

import akka.actor.ActorSystem
import akka.stream.ActorMaterializer
import akka.stream.scaladsl.{Keep, RunnableGraph, Sink, Source}

import scala.concurrent.duration._
import scala.concurrent.{Await, ExecutionContextExecutor, Future}
import scala.util.Success

/** TwoRunnable
 *
 * <pre>
 * See <a href="https://doc.akka.io/docs/akka/current/stream/stream-flows-and-basics.html">Defining and running streams</a>
 *
 * Difference between run and runWith
 * </pre>
 *
 * @author Yuriy Stul
 */
object TwoRunnable extends App {

  val system = ActorSystem.create("TwoRunnable")
  implicit val materializer: ActorMaterializer = ActorMaterializer.create(system)
  implicit val ec: ExecutionContextExecutor = system.dispatcher

  test1()
  test2()

  println("Terminate")

  system.terminate()

  def test1(): Unit = {
    println("==>test1")
    // connect the Source to the Sink, obtaining a RunnableGraph
    val sink = Sink.fold[Int, Int](0)(_ + _)
    val runnable: RunnableGraph[Future[Int]] =
      Source(1 to 10).toMat(sink)(Keep.right)

    // get the materialized value of the FoldSink
    val sum1: Future[Int] = runnable.run()
    val sum2: Future[Int] = runnable.run()

    // sum1 and sum2 are different Futures!
    // sum1 and sum2 have same result!
    sum1.onComplete({
      case Success(result) =>
        println(s"sum1 = $result")
      case _ =>
    })
    sum2.onComplete({
      case Success(result) =>
        println(s"sum2 = $result")
      case _ =>
    })

    Await.result(Future.sequence(Seq(sum1, sum2)), 5.seconds)
    println("<==test1")
  }

  def test2(): Unit = {
    println("==>test2")
    // connect the Source to the Sink, obtaining a RunnableGraph
    val sink = Sink.fold[Int, Int](0)(_ + _)
    val source = Source(1 to 10)

    // get the materialized value of the FoldSink
    val sum1: Future[Int] = source.runWith(sink)
    val sum2: Future[Int] = source.runWith(sink)

    // sum1 and sum2 are different Futures!
    // sum1 and sum2 have same result!
    sum1.onComplete({
      case Success(result) =>
        println(s"sum1 = $result")
      case _ =>
    })
    sum2.onComplete({
      case Success(result) =>
        println(s"sum2 = $result")
      case _ =>
    })

    Await.result(Future.sequence(Seq(sum1, sum2)), 5.seconds)

    println("<==test2")
  }
}
