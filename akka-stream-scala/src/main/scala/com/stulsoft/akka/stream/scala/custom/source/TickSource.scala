/*
 * Copyright (c) 2020. StulSoft
 */

package com.stulsoft.akka.stream.scala.custom.source

import akka.actor.ActorSystem
import akka.stream.stage.{GraphStage, GraphStageLogic, OutHandler}
import akka.stream.{Attributes, Outlet, SourceShape}
import com.typesafe.scalalogging.StrictLogging

import scala.util.Random

/**
 * @author Yuriy Stul
 */
class TickSource(val system: ActorSystem) extends GraphStage[SourceShape[String]] with StrictLogging {
  val out: Outlet[String] = Outlet("TickSource")

  private val random = Random
  private var counter = 0

  override def createLogic(inheritedAttributes: Attributes): GraphStageLogic = new GraphStageLogic(shape) {

    setHandler(out, new OutHandler {
      override def onPull(): Unit = {
        Thread.sleep(123 + random.nextLong(3000))
        counter += 1
        push(out, s"message # $counter")
      }
    })
  }

  override def shape: SourceShape[String] = SourceShape(out)
}
