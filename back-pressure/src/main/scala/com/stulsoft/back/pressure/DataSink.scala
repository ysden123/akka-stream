/*
 * Copyright (c) 2020. StulSoft
 */

package com.stulsoft.back.pressure

import akka.Done
import akka.stream.scaladsl.Sink
import com.typesafe.scalalogging.StrictLogging

import scala.concurrent.Future

/**
 * @author Yuriy Stul
 */
object DataSink extends StrictLogging {

  def sink(): Sink[String, Future[Done]] = {
    Sink.foreach(println)
  }

}
