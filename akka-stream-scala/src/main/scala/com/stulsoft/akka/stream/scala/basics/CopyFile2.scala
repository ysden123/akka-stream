/*
 * Copyright (c) 2020. Yuriy Stul
 */

package com.stulsoft.akka.stream.scala.basics

import java.io.File
import java.nio.file.Paths

import akka.actor.ActorSystem
import akka.stream.IOResult
import akka.stream.scaladsl.{FileIO, Sink, Source}
import akka.util.ByteString

import scala.concurrent.duration._
import scala.concurrent.{Await, ExecutionContextExecutor, Future}
import scala.util.{Failure, Success}

/**
 * @author Yuriy Stul
 */
object CopyFile2 extends App {
  val workFolder = "work"
  implicit val actorSystem: ActorSystem = ActorSystem("CopyFile2")
  implicit val ec: ExecutionContextExecutor = actorSystem.dispatcher

  createWorkFolder()
  copyFile1()
  actorSystem.terminate()

  def createWorkFolder(): Unit = {
    val file = new File(workFolder)
    if (!file.exists())
      file.mkdir()
  }

  // Keep.left
  def copyFile1(): Unit = {
    println("==>CopyFile2::copyFile1")
    try {
      val future = fileSource().to(fileSink()).run()

      val result = Await.result(future, 5.seconds)

      result match {
        case IOResult(count, Success(_)) =>
          println(s"count=$count")
        case IOResult(_, Failure(err)) =>
          println(s"Exception: $err")
      }
    } catch {
      case err: Exception =>
        println(s"Failure: $err")
        System.exit(1)
    }
    println("<==CopyFile2::copyFile1")
  }

  def fileSource(): Source[ByteString, Future[IOResult]] = {
    FileIO
      .fromPath(new File("src/main/resources/testInputFile222.txt").toPath)

    //      .fromPath(new File("src/main/resources/testInputFile.txt").toPath)
  }

  def fileSink(): Sink[ByteString, Future[IOResult]] = {
    FileIO.toPath(Paths.get(s"$workFolder/testFile-copy.txt"))
  }
}
