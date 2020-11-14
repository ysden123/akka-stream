/*
 * Copyright (c) 2020. StulSoft
 */

package com.stulsoft.akka.stream.scala.custom.source

import akka.stream.{Attributes, Outlet, SourceShape}
import akka.stream.stage.{GraphStage, GraphStageLogic, OutHandler}

/**
 * @author Yuriy Stul
 */
class NumbersSource extends GraphStage[SourceShape[Int]] {
  // Define the (sole) output port of this stage
  val out: Outlet[Int] = Outlet("NumbersSource")

  // This is where the actual (possibly stateful) logic will live
  override def createLogic(inheritedAttributes: Attributes): GraphStageLogic = new GraphStageLogic(shape) {
    // All state MUST be inside the GraphStageLogic,
    // never inside the enclosing GraphStage.
    // This state is safe to access and modify from all the
    // callbacks that are provided by GraphStageLogic and the
    // registered handlers.
    private var counter = 1

    setHandler(out, new OutHandler {
      override def onPull(): Unit = {
        push(out, counter)
        counter += 3
      }
    })
  }

  // Define the shape of this stage, which is SourceShape with the port we defined above
  override def shape: SourceShape[Int] = SourceShape(out)
}
