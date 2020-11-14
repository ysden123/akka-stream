/*
 * Copyright (c) 2020. StulSoft
 */

package com.stulsoft.back.pressure

import akka.actor.{ActorSystem, Props}
import akka.stream.Materializer
import com.typesafe.scalalogging.StrictLogging

import scala.concurrent.ExecutionContextExecutor

/**
 * @author Yuriy Stul
 */
object Application extends App with StrictLogging {
  logger.info("==>main")
  val system = ActorSystem.create("BackPressure")
  val materializer = Materializer.createMaterializer(system)
  val ec: ExecutionContextExecutor = system.dispatcher
  val longService = system.actorOf(Props(new LongService), "LongService")

  val source = DataSource.source()
  val flow = new LongServiceFlow(longService).flow()
  val sink = DataSink.sink()
  source.via(flow).runWith(sink)(materializer)

}
