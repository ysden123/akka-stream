/*
 * Copyright (c) 2019. Yuriy Stul
 */

package com.stulsoft.akka.stream.scala.basics

import java.io.File
import java.nio.file.Paths

import akka.actor.ActorSystem
import akka.stream.IOResult
import akka.stream.scaladsl.{FileIO, Keep}

import scala.concurrent.duration._
import scala.concurrent.{Await, ExecutionContextExecutor, Future}
import scala.util.{Failure, Success}

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
  //  copyFileWithError()

  actorSystem.terminate()

  def createWorkFolder(): Unit = {
    val file = new File(workFolder)
    if (!file.exists())
      file.mkdir()
  }

  // Keep.left
  def copyFile1(): Unit = {
    println("==>copyFile1")
    val source = FileIO
      .fromPath(new File("src/main/resources/testInputFile.txt").toPath)
    val sink = FileIO.toPath(Paths.get(s"$workFolder/testFile-copy.txt"))
    val runnableGraph = source.to(sink)
    val future = runnableGraph.run()

    val result = Await.result(future, 5.seconds)

    result.status match {
      case Success(_) =>
        println(s"Success: result.status = ${result.status.get}, result.count = ${result.count}")
      case Failure(exception) =>
        println(s"Failure: result.status = ${result.status},  exception: $exception")
    }
    println("<==copyFile1")
  }

  // Keep.right
  def copyFile2(): Unit = {
    println("==>copyFile2")
    val source = FileIO
      .fromPath(new File("src/main/resources/testInputFile.txt").toPath)
    val sink = FileIO.toPath(Paths.get(s"$workFolder/testFile-copy.txt"))
    val runnableGraph = source.toMat(sink)(Keep.right)
    val future = runnableGraph.run()

    val result = Await.result(future, 5.seconds)

    result.status match {
      case Success(_) =>
        println(s"Success: result.status = ${result.status.get}, result.count = ${result.count}")
      case Failure(exception) =>
        println(s"Failure: result.status = ${result.status},  exception: $exception")
    }
    println("<==copyFile2")
  }

  // Keep.both
  def copyFile3(): Unit = {
    println("==>copyFile3")
    val source = FileIO
      .fromPath(new File("src/main/resources/testInputFile.txt").toPath)
    val sink = FileIO.toPath(Paths.get(s"$workFolder/testFile-copy.txt"))
    val runnableGraph = source.toMat(sink)(Keep.both)
    val future = runnableGraph.run()

    val futureAll = Future.sequence(List(future._1, future._2))
    val result: List[IOResult] = Await.result(futureAll, 5.seconds)

    val inResult = result.head
    inResult.status match {
      case Success(_) =>
        println(s"Success: inResult.status = ${inResult.status.get}, inResult.count = ${inResult.count}")
      case Failure(exception) =>
        println(s"Failure: inResult.status = ${inResult.status},  exception: $exception")
    }

    val outResult = result.last
    outResult.status match {
      case Success(_) =>
        println(s"Success: outResult.status = ${outResult.status.get}, outResult.count = ${outResult.count}")
      case Failure(exception) =>
        println(s"Failure: outResult.status = ${outResult.status},  exception: $exception")
    }

    println("<==copyFile3")
  }

  // todo catch exception!
  /*
    def copyFileWithError(): Unit = {
      println("==>copyFileWithError")
      val source = FileIO
        .fromPath(new File("src/main/resources/testInputFileERROR.txt").toPath)
      val sink = FileIO.toPath(Paths.get(s"$workFolder/testFile-copy.txt"))
      val runnableGraph = source.to(sink)
      val future = runnableGraph.run()
  
      val result = Await.result(future, 5.seconds)
  
      if (result.wasSuccessful)
        println(s"Success: result.status = ${result.status.get}, result.count = ${result.count}")
      else
        println(s"Failure: result.status = ${result.status},  result.getError: ${result.getError}")
      println("<==copyFileWithError")
    }
  */
}
