/*
 * Copyright (c) 2020. Yuriy Stul
 */

package com.stulsoft.akka.stream.scala.basics

import akka.actor.ActorSystem
import akka.stream.ThrottleMode
import akka.stream.scaladsl.Source

import scala.concurrent.Await
import scala.concurrent.duration._

/**
 * @author Yuriy Stul
 */
object ZipWithEx1 extends App {
  implicit val actorSystem: ActorSystem = ActorSystem("ZipWithEx1")

  val r = Source(1 to 10)
    .zipWith(Source('a' to 'z'))((i1, i2) => s"$i1 -> $i2")
    .throttle(1, 500.milliseconds, 1, ThrottleMode.shaping)
    .runForeach(println)

  Await.result(r, 10.seconds)

  actorSystem.terminate()
}
