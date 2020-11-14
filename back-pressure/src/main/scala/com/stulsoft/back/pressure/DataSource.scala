/*
 * Copyright (c) 2020. StulSoft
 */

package com.stulsoft.back.pressure

import akka.actor.Cancellable
import akka.stream.scaladsl.Source
import com.typesafe.scalalogging.StrictLogging

import scala.concurrent.duration.DurationInt

/**
 * @author Yuriy Stul
 */
object DataSource extends StrictLogging {
  def source(): Source[String, Cancellable] = {
    var counter = 0L
    Source.tick[String](0.seconds, 100.milliseconds, "test")
      .map(s => {
        counter += 1
        s"$s $counter"
      })
  }
}
