/*
 * Copyright (c) 2019. Yuriy Stul
 */

package com.stulsoft.akka.stream.scala.composition

import akka.Done
import akka.actor.ActorSystem
import akka.stream.Materializer
import akka.stream.scaladsl.{Keep, Sink, Source}

import scala.concurrent.duration._
import scala.concurrent.{Await, ExecutionContextExecutor, Future, Promise}
import scala.util.{Failure, Success}

/** Composition
 * <p>
 * See <a
 * href="https://doc.akka.io/docs/akka/current/stream/stream-composition.html#basics-of-composition-and-modularity">Basics of composition and modularity</a>
 *
 * @author Yuriy Stul
 */
object CompositionEx1 extends App {
  implicit val actorSystem: ActorSystem = ActorSystem("CompositionEx1")
  implicit val actorMaterializer:Materializer = Materializer.createMaterializer(actorSystem)
  implicit val ec: ExecutionContextExecutor = actorSystem.dispatcher

  val f1 = test1()
  val f2 = test2()
  val f3 = test3()
  val f4 = test4()

  val allFuture = Future.sequence(Seq(f1, f2, f3, f4))
//  val allFuture = Future.sequence(Seq(f1, f2, f3))

  Await.result(allFuture, 1.seconds)

  actorSystem.terminate()
  println("Completed")

  def test1(): Future[Done] = {
    println("==>test1")

    val promise = Promise[Done]

    Source
      .single(0)
      .map(_ + 1)
      .fold(0)(_ + _)
      .runWith(Sink.head)
      .onComplete(result => {
        println(s"test1: Result = ${result.get}")
        promise.success(Done)
        println("<==test1")
      })

    promise.future
  }

  def test2(): Future[Done] = {
    println("==>test2")

    Source
      .single(0)
      .map(_ + 1)
      .fold(0)(_ + _)
      .runForeach(result => {
        println(s"test2: Result = $result")
        println("<==test2")
      })
  }

  def test3(): Future[Done] = {
    println("==>test3")

    val promise = Promise[Done]

    Source
      .single(0)
      .map(_ + 1)
      .fold(0)(_ + _)
      .toMat(Sink.head)(Keep.right)
      .run()
      .onComplete({
        case Success(result) =>
          println(s"test3: Result = $result")
          promise.success(Done)
          println("<==test3")
        case Failure(exception) =>
          println(s"Error: ${exception.getMessage}")
          promise.failure(exception)
          println("<==test3")
      })

    promise.future
  }

  // Throws exception
  def test4(): Future[Done] = {
    println("==>test4")

    val promise = Promise[Done]

    Source
      .single(0)
      .map(_ / 0)
      .recover{
        case e:Exception =>
          println(s"Error 1: ${e.getMessage}")
          -1
      }
      .filter(_ > 0)
      .fold(0)(_ + _)
      .toMat(Sink.head)(Keep.right)
      .run()
      .onComplete({
        case Success(result) =>
          println(s"test4: Result = $result")
          promise.success(Done)
          println("<==test4")
        case Failure(exception) =>
          println(s"Error 2: ${exception.getMessage}")
          promise.failure(exception)
          println("<==test4")
      })

    promise.future
  }
}
