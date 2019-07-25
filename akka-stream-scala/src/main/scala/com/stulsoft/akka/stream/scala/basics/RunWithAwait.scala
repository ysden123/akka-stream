/*
 * Copyright (c) 2019. Yuriy Stul
 */

package com.stulsoft.akka.stream.scala.basics

import akka.Done
import akka.actor.ActorSystem
import akka.stream.ActorMaterializer
import akka.stream.scaladsl.{Keep, Sink, Source}

import scala.concurrent.{Await, Future}
import scala.concurrent.duration._

/**
 * @author Yuriy Stul
 */
object RunWithAwait extends App {
  implicit val actorSystem: ActorSystem = ActorSystem("RunWithAwait")
  implicit val actorMaterializer: ActorMaterializer = ActorMaterializer()

  val items = Seq("1111", "2222", "3333", "2 1111", "2 2222", "2 3333")
  test1()
  test2()
  test3()

  actorSystem.terminate()

  def test1(): Unit = {
    println("==>test1")
    val source = Source.fromIterator(() => items.iterator)
    val sink = Sink.foreach(println)
    val runnable = source.toMat(sink)(Keep.right)
    val future: Future[Done] = runnable.run()
    Await.result(future, 2.seconds)
    println("<==test1")
  }

  // head
  def test2(): Unit = {
    println("==>test2")
    val source = Source.fromIterator(() => items.iterator)
    val runnable = source
      .map(i => {
        println(i)
        i
      })
      .toMat(Sink.head)(Keep.right)

    val future: Future[String] = runnable.run()
    Await.result(future, 2.seconds)
    println("<==test2")
  }
  
  // last
  def test3(): Unit = {
    println("==>test3")
    val source = Source.fromIterator(() => items.iterator)
    val runnable = source
      .map(i => {
        println(i)
        i
      })
      .toMat(Sink.last)(Keep.right)

    val future: Future[String] = runnable.run()
    Await.result(future, 2.seconds)
    println("<==test3")
  }
}
