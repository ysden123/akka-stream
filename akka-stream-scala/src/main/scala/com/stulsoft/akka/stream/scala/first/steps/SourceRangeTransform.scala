/*
 * Copyright (c) 2019. Yuriy Stul
 */

package com.stulsoft.akka.stream.scala.first.steps

import java.nio.file.Paths

import akka.actor.ActorSystem
import akka.stream.ActorMaterializer
import akka.stream.scaladsl._
import akka.util.ByteString
import com.typesafe.scalalogging.LazyLogging

import scala.concurrent.ExecutionContextExecutor

/** First steps. Simple range as source. Usage the transform.
  * <p>
  * See <a href="https://doc.akka.io/docs/akka/2.5/stream/stream-quickstart.html">First steps</a>
  *
  * @author Yuriy Stul
  */
object SourceRangeTransform extends App with LazyLogging {
  logger.info("==>main")
  val system = ActorSystem.create("ScalaSourceRange")
  val materializer = ActorMaterializer.create(system)

  val source = Source[Int](1 to 10)

  val factorials = source.scan(BigInt(1))((acc, next) => acc * next)

  source.runForeach(println)(materializer)

  val result =
    factorials
      .map(num => ByteString(s"num\n"))
      .runWith(FileIO.toPath(Paths.get("test/factorials.txt")))(materializer)

  implicit val ec: ExecutionContextExecutor = system.dispatcher
  result.onComplete(_ => system.terminate())

  logger.info("<==main")

}
