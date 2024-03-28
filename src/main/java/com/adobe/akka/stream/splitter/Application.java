package com.adobe.akka.stream.splitter;

import akka.Done;
import akka.NotUsed;
import akka.actor.ActorSystem;
import akka.stream.FlowShape;
import akka.stream.UniformFanInShape;
import akka.stream.UniformFanOutShape;
import akka.stream.javadsl.*;
import com.adobe.akka.stream.splitter.types.HalfCookedPancake;
import com.adobe.akka.stream.splitter.types.Pancake;
import com.adobe.akka.stream.splitter.types.ScoopOfBatter;

import java.util.List;
import java.util.concurrent.CompletionStage;

class Application {
    public static void main(String[] args) {
        Source<ScoopOfBatter, NotUsed> batterSource = Source.from(
                List.of(
                        new ScoopOfBatter(0), new ScoopOfBatter(1), new ScoopOfBatter(2), new ScoopOfBatter(3)));

        Sink<Pancake, CompletionStage<Done>> sink = Sink.foreach(System.out::println);

        System.out.println("***** Running the akka stream four chefs example *****");
        CompletionStage<Void> fourChefsFuture = run(batterSource, sink);
        fourChefsFuture.toCompletableFuture().join();
        System.out.println("***** Finished running the akka stream four chefs example *****");
        System.exit(0);
    }

    public static CompletionStage<Void> run(Source<ScoopOfBatter, NotUsed> source, Sink<Pancake, CompletionStage<Done>> sink) {
        ActorSystem system = ActorSystem.create("four-chefs");

        Flow<ScoopOfBatter, HalfCookedPancake, NotUsed> fryingPan1 =
                Flow.of(ScoopOfBatter.class)
                        .wireTap(batter -> System.out.println("Frying pan 1: " + batter))
                        .map(HalfCookedPancake::fromBatter);
        Flow<HalfCookedPancake, Pancake, NotUsed> fryingPan2 =
                Flow.of(HalfCookedPancake.class)
                        .wireTap(halfCooked -> System.out.println("Frying pan 2: " + halfCooked))
                        .map(HalfCookedPancake::complete);
        Flow<ScoopOfBatter, HalfCookedPancake, NotUsed> pancakeChefs1 =
                Flow.fromGraph(
                        GraphDSL.create(
                                b -> {
                                    final UniformFanInShape<HalfCookedPancake, HalfCookedPancake> mergeHalfCooked =
                                            b.add(Merge.create(2));
                                    final UniformFanOutShape<ScoopOfBatter, ScoopOfBatter> dispatchBatter =
                                            b.add(Balance.create(2));

                                    // Two chefs work with one frying pan for each, half-frying the pancakes then
                                    // putting
                                    // them into a common pool
                                    b.from(dispatchBatter.out(0))
                                            .via(b.add(fryingPan1.async()))
                                            .toInlet(mergeHalfCooked.in(0));
                                    b.from(dispatchBatter.out(1))
                                            .via(b.add(fryingPan1.async()))
                                            .toInlet(mergeHalfCooked.in(1));

                                    return FlowShape.of(dispatchBatter.in(), mergeHalfCooked.out());
                                }));

        Flow<HalfCookedPancake, Pancake, NotUsed> pancakeChefs2 =
                Flow.fromGraph(
                        GraphDSL.create(
                                b -> {
                                    final UniformFanInShape<Pancake, Pancake> mergePancakes = b.add(Merge.create(2));
                                    final UniformFanOutShape<HalfCookedPancake, HalfCookedPancake>
                                            dispatchHalfCooked = b.add(Balance.create(2));

                                    // Two chefs work with one frying pan for each, finishing the pancakes then
                                    // putting
                                    // them into a common pool
                                    b.from(dispatchHalfCooked.out(0))
                                            .via(b.add(fryingPan2.async()))
                                            .toInlet(mergePancakes.in(0));
                                    b.from(dispatchHalfCooked.out(1))
                                            .via(b.add(fryingPan2.async()))
                                            .toInlet(mergePancakes.in(1));

                                    return FlowShape.of(dispatchHalfCooked.in(), mergePancakes.out());
                                }));

        Flow<ScoopOfBatter, Pancake, NotUsed> kitchen = pancakeChefs1.via(pancakeChefs2);
        CompletionStage<Done> done = source
                .viaMat(kitchen, Keep.right())
                .toMat(sink, Keep.right())
                .run(system);
        return done
                .thenRun(system::terminate)
                .thenRun(() -> System.out.println("Pancakes are done!"));
    }
}
