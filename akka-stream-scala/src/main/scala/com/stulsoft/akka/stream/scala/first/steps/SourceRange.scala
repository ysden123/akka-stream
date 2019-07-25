/*
 * Copyright (c) 2019. Yuriy Stul
 */

package com.stulsoft.akka.stream.scala.first.steps

import akka.actor.ActorSystem
import akka.stream.ActorMaterializer
import akka.stream.scaladsl._
import com.typesafe.scalalogging.LazyLogging

import scala.concurrent.ExecutionContextExecutor

/** First steps. Simple range as source
  * <p>
  * See <a href="https://doc.akka.io/docs/akka/2.5/stream/stream-quickstart.html">First steps</a>
  *
  * @author Yuriy Stul
  */
object SourceRange extends App with LazyLogging {
  logger.info("==>main")
  val system = ActorSystem.create("ScalaSourceRange")
  val materializer = ActorMaterializer.create(system)

  val source = Source[Int](1 to 10)

  val done = source.runForeach(println)(materializer)

  implicit val ec: ExecutionContextExecutor = system.dispatcher
  done.onComplete(_ => system.terminate())

  logger.info("<==main")
}
