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

import scala.concurrent.Await
import scala.concurrent.duration.DurationInt

/**
 * @author Yuriy Stul
 */
object LongServiceFlow extends StrictLogging {
  def flow(longService: ActorRef): Flow[String, String, NotUsed] = {
    implicit val timeout: Timeout = Timeout(10 seconds)
    Flow[String].map(msg => {
      val future = longService ? LongService.Message(msg)
      val result = Await.result(future, 10 seconds)
      result match {
        case response: String =>
          logger.debug("response: {}", response)
          response
      }
    })
  }
}
