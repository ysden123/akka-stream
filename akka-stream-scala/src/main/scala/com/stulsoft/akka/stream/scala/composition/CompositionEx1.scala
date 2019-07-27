/*
 * Copyright (c) 2019. Yuriy Stul
 */

package com.stulsoft.akka.stream.scala.composition

import akka.actor.ActorSystem
import akka.stream.ActorMaterializer
import akka.stream.scaladsl.{Sink, Source}

import scala.concurrent.ExecutionContextExecutor
import scala.concurrent.duration._

/** Composition
 * <p>
 * See <a
 * href="https://doc.akka.io/docs/akka/current/stream/stream-composition.html#basics-of-composition-and-modularity">Basics of composition and modularity</a>
 *
 * @author Yuriy Stul
 */
object CompositionEx1 extends App {
  implicit val actorSystem: ActorSystem = ActorSystem("CompositionEx1")
  implicit val actorMaterializer: ActorMaterializer = ActorMaterializer()
  implicit val ec: ExecutionContextExecutor = actorSystem.dispatcher

  test1()

  actorSystem.scheduler.scheduleOnce(1.seconds)({
    actorSystem.terminate()
    println("Completed")
  })

  def test1(): Unit = {
    println("==>test1")

    Source
      .single(0)
      .map(_ + 1)
      .fold(0)(_ + _)
      .runWith(Sink.head)
      .onComplete(result => {
        println(s"Result = ${result.get}")
      })

    println("<==test1")
  }
}
