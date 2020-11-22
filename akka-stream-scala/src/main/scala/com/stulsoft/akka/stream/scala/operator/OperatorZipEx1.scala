/*
 * Copyright (c) 2020. Webpals
 */

package com.stulsoft.akka.stream.scala.operator

import akka.actor.ActorSystem
import akka.stream.Materializer
import akka.stream.scaladsl.Source

import scala.concurrent.ExecutionContextExecutor

/**
 * @author Yuriy Stul
 */
object OperatorZipEx1 extends App {
  val system = ActorSystem.create("OperatorZipEx1")
  implicit val materializer: Materializer = Materializer.createMaterializer(system)
  implicit val ec: ExecutionContextExecutor = system.dispatcher

  val numbers = Source(1 :: 2 :: 3 :: 4 :: Nil)
//  val letters = Source("a" :: "b" :: "c" :: Nil)
  val letters = Source("a" :: "b" :: Nil)

  numbers
    .zipAll(letters, "for missing values", "default")
    .runForeach(println)

}
