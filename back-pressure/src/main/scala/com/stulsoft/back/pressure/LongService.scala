/*
 * Copyright (c) 2020. StulSoft
 */

package com.stulsoft.back.pressure

import akka.actor.{Actor, ActorLogging}
import com.stulsoft.back.pressure.LongService.Message

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration.DurationInt
import scala.util.Random

/**
 * @author Yuriy Stul
 */
class LongService extends Actor with ActorLogging {
  private lazy val random = Random

  override def receive: Receive = {
    case Message(msg) =>
      log.info("Started handling {}", msg)
      val theSender = sender()
      context.system.scheduler.scheduleOnce((123 + random.nextInt(1000)) milliseconds) {
        val response = s"Response for $msg"
        log.info("Sending response: {}", response)
        theSender ! response
      }
  }
}

object LongService {

  case class Message(body: String)

}
