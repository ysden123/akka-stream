/*
 * Copyright (c) 2019. Yuriy Stul
 */

package com.stulsoft.akka.stream.java.first.steps;

import akka.NotUsed;
import akka.actor.ActorSystem;
import akka.stream.ActorMaterializer;
import akka.stream.IOResult;
import akka.stream.Materializer;
import akka.stream.javadsl.FileIO;
import akka.stream.javadsl.Source;
import akka.util.ByteString;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigInteger;
import java.nio.file.Paths;
import java.util.concurrent.CompletionStage;

/**
 * First steps. Simple range as source. Usage the transform.
 * <p>
 * See <a href="https://doc.akka.io/docs/akka/2.5/stream/stream-quickstart.html">First steps</a>
 * </p>
 *
 * @author Yuriy Stul
 */
public class SourceRangeTransform {
    private static final Logger logger = LoggerFactory.getLogger(SourceRangeTransform.class);

    public static void main(String[] args) {
        logger.info("==>main");
        final ActorSystem system = ActorSystem.create("JavaSourceRange");
        final Materializer materializer = ActorMaterializer.create(system);

        final Source<Integer, NotUsed> source = Source.range(1, 10);

        final Source<BigInteger, NotUsed> factorials =
                source.scan(BigInteger.ONE, (acc, next) -> acc.multiply(BigInteger.valueOf(next)));

        source.runForeach(System.out::println, materializer);

        final CompletionStage<IOResult> result =
                factorials
                        .map(num -> ByteString.fromString(num.toString() + "\n"))
                        .runWith(FileIO.toPath(Paths.get("test/factorials.txt")), materializer);

        result.thenRun(system::terminate);

        logger.info("<==main");
    }
}
