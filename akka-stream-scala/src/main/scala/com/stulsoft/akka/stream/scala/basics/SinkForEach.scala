/*
 * Copyright (c) 2019. Yuriy Stul
 */

package com.stulsoft.akka.stream.scala.basics

import akka.actor.ActorSystem
import akka.stream.Materializer
import akka.stream.scaladsl.{Sink, Source}

import scala.concurrent.duration._
import scala.concurrent.{Await, ExecutionContextExecutor}

/** SinkForEach
 *
 * @author Yuriy Stul
 */
object SinkForEach extends App {
  val system = ActorSystem.create("TwoRunnable")

  implicit val materializer:Materializer = Materializer.createMaterializer(system)
  implicit val ec: ExecutionContextExecutor = system.dispatcher

  test1()

  system.terminate()

  def test1(): Unit = {
    println("==>test1")
    val source = Source.fromIterator(() => List("111", "222").iterator)
    Await.result(source.runWith(Sink.foreach(println(_))), 5.seconds)
    println("<==test1")
  }
}
