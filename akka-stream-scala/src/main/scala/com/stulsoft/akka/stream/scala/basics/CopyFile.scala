/*
 * Copyright (c) 2019. Yuriy Stul
 */

package com.stulsoft.akka.stream.scala.basics

import java.io.File
import java.nio.file.Paths

import akka.actor.ActorSystem
import akka.stream.IOResult
import akka.stream.scaladsl.{FileIO, Keep, Sink, Source}
import akka.util.ByteString

import scala.concurrent.duration._
import scala.concurrent.{Await, ExecutionContextExecutor, Future}

/**
 * @author Yuriy Stul
 */
object CopyFile extends App {
  val workFolder = "work"
  implicit val actorSystem: ActorSystem = ActorSystem("CopyFile")
  implicit val ec: ExecutionContextExecutor = actorSystem.dispatcher

  createWorkFolder()
  copyFile1()
  copyFile2()
  copyFile3()

  actorSystem.terminate()

  def createWorkFolder(): Unit = {
    val file = new File(workFolder)
    if (!file.exists())
      file.mkdir()
  }

  def showResult(result: IOResult): Unit = {
    if (result.count > 0)
      println(s"Success: result.count = ${result.count}")
    else
      println(s"Failure")
  }

  // Keep.left
  def copyFile1(): Unit = {
    println("==>copyFile1")
    val future = fileSource().to(fileSink()).run()

    showResult(Await.result(future, 5.seconds))

    println("<==copyFile1")
  }

  // Keep.right
  def copyFile2(): Unit = {
    println("==>copyFile2")
    val future = fileSource().toMat(fileSink())(Keep.right).run()

    showResult(Await.result(future, 5.seconds))

    println("<==copyFile2")
  }

  // Keep.both
  def copyFile3(): Unit = {
    println("==>copyFile3")
    val future = fileSource().toMat(fileSink())(Keep.both).run()

    val futureAll = Future.sequence(List(future._1, future._2))
    val result: List[IOResult] = Await.result(futureAll, 5.seconds)

    showResult(result.head) // inResult

    showResult(result.last) // outResult

    println("<==copyFile3")
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
