/*
 * Copyright (c) 2019. Yuriy Stul
 */

package com.stulsoft.akka.stream.scala.first.steps

import java.nio.file.Paths

import akka.actor.ActorSystem
import akka.stream.Materializer
import akka.stream.scaladsl._
import akka.util.ByteString
import com.typesafe.scalalogging.StrictLogging

import scala.concurrent.ExecutionContextExecutor

/** First steps. Simple range as source. Usage the transform.
  * <p>
  * See <a href="https://doc.akka.io/docs/akka/2.5/stream/stream-quickstart.html">First steps</a>
  *
  * @author Yuriy Stul
  */
object SourceRangeTransform extends App with StrictLogging {
  logger.info("==>main")
  val testFolder = "test"
  val testFolderFile = Paths.get(testFolder).toFile
  if (!testFolderFile.exists()) {
    try {
      testFolderFile.mkdir()
    } catch {
      case e: Exception => logger.error("Cannot create directory {} - {}", testFolder, e.getMessage)
        System.exit(1)
    }
  }
  val system = ActorSystem.create("SourceRangeTransform")
  implicit val materializer:Materializer = Materializer.createMaterializer(system)

  val source = Source[Int](1 to 10)

  val factorials = source.scan(BigInt(1))((acc, next) => acc * next)

  source.runForeach(println)

  val result =
    factorials
      .map(num => ByteString(s"$num\n"))
      .runWith(FileIO.toPath(Paths.get("test/factorials.txt")))

  implicit val ec: ExecutionContextExecutor = system.dispatcher
  result.onComplete(_ => {
    logger.info("<==main")
    system.terminate()
  })
}
