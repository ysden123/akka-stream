/*
 * Copyright (c) 2020. StulSoft
 */

package com.stulsoft.akka.stream.scala.custom.source

import akka.actor.ActorSystem
import akka.stream.stage.{GraphStage, GraphStageLogic, OutHandler}
import akka.stream.{Attributes, Outlet, SourceShape}
import com.typesafe.scalalogging.StrictLogging

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration.DurationInt
import scala.concurrent.{Await, Future, Promise}
import scala.util.{Failure, Random, Success}

/**
 * @author Yuriy Stul
 */
class TickSourceF(val system: ActorSystem) extends GraphStage[SourceShape[String]] with StrictLogging {
  val out: Outlet[String] = Outlet("TickSource")

  private val random = Random
  private var counter = 0

  override def createLogic(inheritedAttributes: Attributes): GraphStageLogic = new GraphStageLogic(shape) {

    setHandler(out, new OutHandler {
      override def onPull(): Unit = {
        val futureWithMessage = buildMessage()
        val message = Await.result(futureWithMessage, 5.seconds)
        push(out, message)
      }
    })

    def buildMessage(): Future[String] = {
      val promise = Promise[String]()
      val f = Future {
        Thread.sleep(123 + random.nextLong(3000))
        counter += 1
        val message = s"message # $counter"
        message
      }
      f.onComplete {
        case Success(message) =>
          promise.complete(Success(message))
        case Failure(err) =>
          promise.failure(err)
      }
      promise.future
    }
  }

  override def shape: SourceShape[String] = SourceShape(out)
}
