package com.adobe.akka.stream.splitter;

import akka.Done;
import akka.NotUsed;
import akka.actor.ActorSystem;
import akka.stream.FlowShape;
import akka.stream.UniformFanInShape;
import akka.stream.UniformFanOutShape;
import akka.stream.javadsl.*;
import com.adobe.akka.stream.splitter.types.*;

import java.util.List;
import java.util.concurrent.CompletionStage;

class Application {
    public static void main(String[] args) {

        // Execute the four chefs example
        Source<ScoopOfBatter, NotUsed> batterSource = Source.from(
                List.of(
                        new ScoopOfBatter(0), new ScoopOfBatter(1), new ScoopOfBatter(2), new ScoopOfBatter(3)));

        Sink<Pancake, CompletionStage<Done>> sink = Sink.foreach(pancake -> System.out.println("Pancake ready: " + pancake));

        System.out.println("***** Running the akka stream four chefs example *****");
        CompletionStage<Void> fourChefsFuture = run(batterSource, sink);
        fourChefsFuture.toCompletableFuture().join();
        System.out.println("***** Finished running the akka stream four chefs example *****\n\n");
        // Complete the four chefs example

        // Execute the splitter example
        Source<PancakeOrder, NotUsed> orderSource = Source.from(
                List.of(
                        new PancakeOrder(0, PancakeAddIn.BLUEBERRY),
                        new PancakeOrder(1, PancakeAddIn.BANANA),
                        new PancakeOrder(2, PancakeAddIn.CHOCOLATE_CHIP),
                        new PancakeOrder(3, PancakeAddIn.BLUEBERRY),
                        new PancakeOrder(4, PancakeAddIn.BANANA),
                        new PancakeOrder(5, PancakeAddIn.CHOCOLATE_CHIP),
                        new PancakeOrder(6, PancakeAddIn.BLUEBERRY),
                        new PancakeOrder(7, PancakeAddIn.BANANA),
                        new PancakeOrder(8, PancakeAddIn.CHOCOLATE_CHIP)));
        System.out.println("***** Running the akka stream splitter example *****");
        CompletionStage<Void> splitterFuture = runSplitter(orderSource, sink);
        splitterFuture.toCompletableFuture().join();
        System.out.println("***** Finished running the akka stream splitter example *****");
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

    public static CompletionStage<Void> runSplitter(Source<PancakeOrder, NotUsed> source, Sink<Pancake, CompletionStage<Done>> sink) {
        ActorSystem system = ActorSystem.create("splitter");

        Flow<PancakeOrder, ScoopOfBlueberryBatter, NotUsed> blueberryBatterFlow =
                Flow.of(PancakeOrder.class)
                        .filter(order -> order.getAddIn() == PancakeAddIn.BLUEBERRY)
                        .map(order -> new ScoopOfBlueberryBatter(order.getIndex()));

        Flow<PancakeOrder, ScoopOfBananaBatter, NotUsed> bananaBatterFlow =
                Flow.of(PancakeOrder.class)
                        .filter(order -> order.getAddIn() == PancakeAddIn.BANANA)
                        .map(order -> new ScoopOfBananaBatter(order.getIndex()));

        Flow<PancakeOrder, ScoopOfChocolateChipBatter, NotUsed> chocolateChipBatterFlow =
                Flow.of(PancakeOrder.class)
                        .filter(order -> order.getAddIn() == PancakeAddIn.CHOCOLATE_CHIP)
                        .map(order -> new ScoopOfChocolateChipBatter(order.getIndex()));

        Flow<PancakeOrder, ScoopOfBatter, NotUsed> prepareBatter = Flow.fromGraph(GraphDSL.create(builder -> {
            final UniformFanInShape<ScoopOfBatter, ScoopOfBatter> mergeOrders = builder.add(Merge.create(3));
            final UniformFanOutShape<PancakeOrder, PancakeOrder> dispatchOrders = builder.add(Broadcast.create(3));

            // Three chefs work together to prepare a made to order batter.
            // A chef is assigned to a specific type of batter and a copy of all pancake orders go to each chef.
            // A chef will only prepare a batter if the order is for their assigned type of batter,
            // otherwise the order is dropped.
            builder.from(dispatchOrders)
                    .via(builder.add(blueberryBatterFlow.async()))
                    .toInlet(mergeOrders.in(0));
            builder.from(dispatchOrders)
                    .via(builder.add(bananaBatterFlow.async()))
                    .toInlet(mergeOrders.in(1));
            builder.from(dispatchOrders)
                    .via(builder.add(chocolateChipBatterFlow.async()))
                    .toInlet(mergeOrders.in(2));

            return FlowShape.of(dispatchOrders.in(), mergeOrders.out());
        }));

        Flow<PancakeOrder, HalfCookedPancake, NotUsed> fryingPan1 =
                Flow.of(PancakeOrder.class).via(prepareBatter)
                        .map(HalfCookedPancake::fromBatter);

        Flow<HalfCookedPancake, Pancake, NotUsed> fryingPan2 =
                Flow.of(HalfCookedPancake.class)
                        .map(HalfCookedPancake::complete);


        Flow<PancakeOrder, HalfCookedPancake, NotUsed> pancakeChefs1 = Flow.fromGraph(GraphDSL.create(builder -> {
            final UniformFanInShape<HalfCookedPancake, HalfCookedPancake> mergeHalfCooked = builder.add(Merge.create(2));
            final UniformFanOutShape<PancakeOrder, PancakeOrder> dispatchBatter = builder.add(Balance.create(2));

            // Two chefs work with one frying pan for each, half-frying the pancakes then
            // putting
            // them into a common pool
            builder.from(dispatchBatter.out(0))
                    .via(builder.add(fryingPan1.async()))
                    .toInlet(mergeHalfCooked.in(0));
            builder.from(dispatchBatter.out(1))
                    .via(builder.add(fryingPan1.async()))
                    .toInlet(mergeHalfCooked.in(1));

            return FlowShape.of(dispatchBatter.in(), mergeHalfCooked.out());
        }));

        Flow<HalfCookedPancake, Pancake, NotUsed> pancakeChefs2 = Flow.fromGraph(GraphDSL.create(builder -> {
            final UniformFanInShape<Pancake, Pancake> mergePancakes = builder.add(Merge.create(2));
            final UniformFanOutShape<HalfCookedPancake, HalfCookedPancake> dispatchHalfCooked = builder.add(Balance.create(2));

            // Two chefs work with one frying pan for each, finishing the pancakes then
            // putting
            // them into a common pool
            builder.from(dispatchHalfCooked.out(0))
                    .via(builder.add(fryingPan2.async()))
                    .toInlet(mergePancakes.in(0));
            builder.from(dispatchHalfCooked.out(1))
                    .via(builder.add(fryingPan2.async()))
                    .toInlet(mergePancakes.in(1));

            return FlowShape.of(dispatchHalfCooked.in(), mergePancakes.out());
        }));

        Flow<PancakeOrder, Pancake, NotUsed> kitchen = pancakeChefs1.via(pancakeChefs2);
        CompletionStage<Done> done = source.
                viaMat(kitchen, Keep.right())
                .toMat(sink, Keep.right())
                .run(system);
        return done
                .thenRun(system::terminate)
                .thenRun(() -> System.out.println("Pancakes are done!"));
    }
}
