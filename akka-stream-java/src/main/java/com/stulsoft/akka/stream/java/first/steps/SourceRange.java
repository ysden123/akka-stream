/*
 * Copyright (c) 2019. Yuriy Stul
 */

package com.stulsoft.akka.stream.java.first.steps;

import akka.Done;
import akka.NotUsed;
import akka.actor.ActorSystem;
import akka.stream.ActorMaterializer;
import akka.stream.Materializer;
import akka.stream.javadsl.Source;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.CompletionStage;

/**
 * First steps. Simple range as source
 * <p>
 * See <a href="https://doc.akka.io/docs/akka/2.5/stream/stream-quickstart.html">First steps</a>
 * </p>
 *
 * @author Yuriy Stul
 */
public class SourceRange {
    private static final Logger logger = LoggerFactory.getLogger(SourceRange.class);

    public static void main(String[] args) {
        logger.info("==>main");
        final ActorSystem system = ActorSystem.create("QuickStart");
        final Materializer materializer = ActorMaterializer.create(system);

        final Source<Integer, NotUsed> source = Source.range(1, 10);
        source.runForeach(System.out::println, materializer);

        final CompletionStage<Done> done = source.runForeach(System.out::println, materializer);
        done.thenRun(system::terminate);
        logger.info("<==main");
    }
}
