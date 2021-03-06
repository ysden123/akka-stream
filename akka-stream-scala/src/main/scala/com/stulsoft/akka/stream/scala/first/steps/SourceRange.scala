/*
 * Copyright (c) 2019. Yuriy Stul
 */

package com.stulsoft.akka.stream.scala.first.steps

import akka.actor.ActorSystem
import akka.stream.Materializer
import akka.stream.scaladsl._
import com.typesafe.scalalogging.StrictLogging

import scala.concurrent.ExecutionContextExecutor

/** First steps. Simple range as source
 * <p>
 * See <a href="https://doc.akka.io/docs/akka/2.5/stream/stream-quickstart.html">First steps</a>
 *
 * @author Yuriy Stul
 */
object SourceRange extends App with StrictLogging {
  logger.info("==>main")
  val system = ActorSystem.create("ScalaSourceRange")
  implicit val materializer: Materializer = Materializer.createMaterializer(system)
  val source = Source[Int](1 to 10)

  val done = source.runForeach(println)

  implicit val ec: ExecutionContextExecutor = system.dispatcher
  done.onComplete(_ => {
    logger.info("<==main")
    system.terminate()
  })
}
