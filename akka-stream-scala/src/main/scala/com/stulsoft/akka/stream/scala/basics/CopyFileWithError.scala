/*
 * Copyright (c) 2020. Yuriy Stul
 */

package com.stulsoft.akka.stream.scala.basics

import java.io.File
import java.nio.file.Paths

import akka.actor.ActorSystem
import akka.stream.IOResult
import akka.stream.scaladsl.FileIO

import scala.concurrent.duration._
import scala.concurrent.{Await, ExecutionContextExecutor, Future, Promise}
import scala.util.{Failure, Success, Try}

/** Error handling
 *
 * @author Yuriy Stul
 */
object CopyFileWithError extends App {
  val workFolder = "work"
  implicit val actorSystem: ActorSystem = ActorSystem("CopyFileWithError")
  implicit val ec: ExecutionContextExecutor = actorSystem.dispatcher

  createWorkFolder()

  try {
    Await.result(copyFileWithoutError(), 5.seconds) match {
      case Success(_) =>
        println("Without error: done")
      case Failure(exc) =>
        println(s"Without error, exception: $exc")
    }
  } catch {
    case e: Exception =>
      println(s"Without error, global exception: $e")
  }

  try {
    Await.result(copyFileWithError(), 5.seconds) match {
      case Success(_) =>
        println("With error: done")
      case Failure(exc) =>
        println(s"With error, exception: $exc")
    }
  } catch {
    case e: Exception =>
      println(s"With error, global exception: $e")
  }

  actorSystem.terminate()

  def createWorkFolder(): Unit = {
    val file = new File(workFolder)
    if (!file.exists())
      file.mkdir()
  }

  def copyFileWithoutError(): Future[Try[Boolean]] = {
    println("==>copyFileWithoutError")
    copyFile("src/main/resources/testInputFile.txt")
  }

  def copyFileWithError(): Future[Try[Boolean]] = {
    println("==>copyFileWithError")
    copyFile("src/main/resources/testInputFileERROR.txt")
  }

  def copyFile(pathname: String): Future[Try[Boolean]] = {
    val promise = Promise[Try[Boolean]]
    val source = FileIO
      .fromPath(new File(pathname).toPath)
    val sink = FileIO.toPath(Paths.get(s"$workFolder/testFile-copy.txt"))
    val runnableGraph = source
//      .recover {
//        case e: Exception =>
//          println(s"Failed, error: $e")
//          throw e
//      }
      .to(sink)

    try {
      runnableGraph
        .run()
        .onComplete {
          case Success(value) =>
            println(s"onComplete, value is $value")

            value match {
              case IOResult(_, Success(_)) =>
                println("Mark success")
                promise.success(Success(true))
              case IOResult(_, Failure(ex)) =>
                println("Mark failure")
                promise.failure(ex)
            }
            println("(1) <==copyFile")
          case Failure(exception) =>
            println(s"onComplete, exception is $exception")
            promise.failure(exception)
            println("(2) <==copyFile")
        }
    } catch {
      case e: Exception =>
        println(s"(M) Exception $e")
        promise.failure(e)
    }

    promise.future
  }
}
