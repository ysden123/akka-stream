/*
 * Copyright (c) 2020. StulSoft
 */

package com.stulsoft.back.pressure

import akka.NotUsed
import akka.actor.ActorRef
import akka.pattern.ask
import akka.stream.scaladsl.Flow
import akka.util.Timeout
import com.typesafe.scalalogging.StrictLogging

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration.DurationInt
import scala.concurrent.{Future, Promise}
import scala.util.{Failure, Success}

/**
 * @author Yuriy Stul
 */
class LongServiceFlow(val longService: ActorRef) extends StrictLogging {
  implicit val timeout: Timeout = Timeout(10 seconds)

  def flow(): Flow[String, String, NotUsed] = {
    Flow[String].mapAsync[String](1)(msg => callLongService(msg))
  }

  private def callLongService(msg: String): Future[String] = {
    val promise = Promise[String]()
    (longService ? LongService.Message(msg))
      .onComplete {
        case Success(response: String) =>
          promise.success(response)
        case _ =>
      }

    promise.future
  }
}
