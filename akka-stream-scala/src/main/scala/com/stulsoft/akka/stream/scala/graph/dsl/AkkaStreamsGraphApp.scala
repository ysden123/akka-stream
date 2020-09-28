/*
 * Copyright (c) 2019. Yuriy Stul
 */

package com.stulsoft.akka.stream.scala.graph.dsl

import akka.NotUsed
import akka.actor.ActorSystem
import akka.stream.ClosedShape
import akka.stream.scaladsl.{Broadcast, Flow, GraphDSL, Merge, RunnableGraph, Sink, Source}
import com.typesafe.scalalogging.StrictLogging

/** Akka Stream Graph
  *
  * @author Yuriy Stul
  */
object AkkaStreamsGraphApp extends App with StrictLogging {
  logger.info("==>main")

  implicit val actorSystem: ActorSystem = ActorSystem("NumberSystem")
  val graph = RunnableGraph.fromGraph(GraphDSL.create() {
    implicit builder: GraphDSL.Builder[NotUsed] =>
      import GraphDSL.Implicits._
      val in = Source(1 to 10)
      val out = Sink.foreach(println)
      val broadCast = builder.add(Broadcast[Int](2))
      val merge = builder.add(Merge[Int](2))
      val f1, f2, f3, f4 = Flow[Int].map(_ + 1)
      val f5 = Flow[Int].filter(x => x % 2 == 0)
      // @formatter:off
      in ~> f1 ~> broadCast ~> f2 ~> merge ~> f4 ~> f5 ~> out
                  broadCast ~> f3 ~> merge
      // @formatter:on
      ClosedShape
  })
  graph.run
  Thread.sleep(1000)
  actorSystem.terminate
  logger.info("<==main")

}
