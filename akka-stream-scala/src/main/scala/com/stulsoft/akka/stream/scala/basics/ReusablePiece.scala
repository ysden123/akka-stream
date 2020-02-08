/*
 * Copyright (c) 2020. Yuriy Stul
 */

package com.stulsoft.akka.stream.scala.basics

import java.io.File
import java.nio.file.Paths

import akka.actor.ActorSystem
import akka.stream.IOResult
import akka.stream.scaladsl.{FileIO, Flow, Keep, Sink, Source}
import akka.util.ByteString

import scala.concurrent.duration._
import scala.concurrent.{Await, ExecutionContextExecutor, Future}

/**
 * @author Yuriy Stul
 */
object ReusablePiece extends App {
  implicit val actorSystem: ActorSystem = ActorSystem("ReusablePiece")
  implicit val ec: ExecutionContextExecutor = actorSystem.dispatcher

  val workFolder = "work"

  createWorkFolder()

  val source = Source.fromIterator(() => Seq("111", "222", "333").iterator)
  val result = source.runWith(lineSink(workFolder + "/reusable-pieces-1.txt"))
  Await.result(result, 5.seconds)

  Await.result(Source.fromIterator(() => Seq("aaa", "bbb", "ccc").iterator)
    .runWith(lineSink(workFolder + "/reusable-pieces-2.txt")),
    5.seconds)

  actorSystem.terminate()

  def createWorkFolder(): Unit = {
    val file = new File(workFolder)
    if (!file.exists())
      file.mkdir()
  }

  /**
   * Reusable piece
   * <p>
   * Saves items in a file.
   */
  def lineSink(filename: String): Sink[String, Future[IOResult]] =
    Flow[String].map(s => ByteString(s + "\n")).toMat(FileIO.toPath(Paths.get(filename)))(Keep.right)
}
