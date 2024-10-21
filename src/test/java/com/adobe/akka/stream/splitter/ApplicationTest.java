package com.adobe.akka.stream.splitter;

import akka.Done;
import akka.NotUsed;
import akka.actor.ActorSystem;
import akka.stream.javadsl.Sink;
import akka.stream.javadsl.Source;
import akka.stream.testkit.TestPublisher;
import akka.stream.testkit.TestSubscriber;
import com.adobe.akka.stream.splitter.types.*;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ApplicationTest {


    @Test
    void givenScoopOfBatter_whenRun_thenReturnPancake() {
        // Arrange
        ActorSystem system = ActorSystem.create("four-chefs-test");

        TestPublisher.Probe<ScoopOfBatter> sourceProbe = new TestPublisher.Probe<>(1, system);
        Source<ScoopOfBatter, NotUsed> source = Source.fromPublisher(sourceProbe);

        CompletableFuture<Done> completableFuture = new CompletableFuture<>();
        TestSubscriber.Probe<Pancake> sinkProbe = new TestSubscriber.Probe<>(system);
        Sink<Pancake, CompletionStage<Done>> sink = Sink.fromSubscriber(sinkProbe)
                .mapMaterializedValue(done -> completableFuture);

        // Act
        CompletionStage<Void> runComplete = Application.run(source, sink);
        sourceProbe.sendNext(new ScoopOfBatter(0));
        sourceProbe.sendComplete();
        Pancake pancake = sinkProbe.requestNext();

        // Assert
        sinkProbe.expectComplete();
        assertEquals("Pancake(0)", pancake.toString());

        completableFuture.complete(Done.getInstance());
        assertTrue(runComplete.toCompletableFuture().isDone());
    }

    @Test
    void givenFourScoopOfBatter_whenRun_thenReturnPancakeInAnyOrderSent() {
        // Arrange
        ActorSystem system = ActorSystem.create("four-chefs-test");

        TestPublisher.Probe<ScoopOfBatter> sourceProbe = new TestPublisher.Probe<>(1, system);
        Source<ScoopOfBatter, NotUsed> source = Source.fromPublisher(sourceProbe);

        CompletableFuture<Done> completableFuture = new CompletableFuture<>();
        TestSubscriber.Probe<Pancake> sinkProbe = new TestSubscriber.Probe<>(system);
        Sink<Pancake, CompletionStage<Done>> sink = Sink.fromSubscriber(sinkProbe)
                .mapMaterializedValue(done -> completableFuture);

        // Act
        CompletionStage<Void> runComplete = Application.run(source, sink);
        sourceProbe.sendNext(new ScoopOfBatter(0));
        sourceProbe.sendNext(new ScoopOfBatter(3));
        sourceProbe.sendNext(new ScoopOfBatter(2));
        sourceProbe.sendNext(new ScoopOfBatter(1));
        sourceProbe.sendComplete();
        Pancake pancake0 = sinkProbe.requestNext();
        Pancake pancake1 = sinkProbe.requestNext();
        Pancake pancake2 = sinkProbe.requestNext();
        Pancake pancake3 = sinkProbe.requestNext();

        // Assert
        sinkProbe.expectComplete();
        List<String> pancakes =
                List.of(pancake0.toString(), pancake1.toString(), pancake2.toString(), pancake3.toString());
        assertThat("Pancakes are returned without order",
                pancakes, containsInAnyOrder("Pancake(0)", "Pancake(3)", "Pancake(2)", "Pancake(1)"));

        completableFuture.complete(Done.getInstance());
        assertTrue(runComplete.toCompletableFuture().isDone());
    }

    @Test
    void givenABlueberryPancakeOrder_whenRunSplitter_thenReturnBlueberryPancake() {
        // Arrange
        ActorSystem system = ActorSystem.create("splitter-test");

        TestPublisher.Probe<PancakeOrder> sourceProbe = new TestPublisher.Probe<>(1, system);
        Source<PancakeOrder, NotUsed> source = Source.fromPublisher(sourceProbe);

        CompletableFuture<Done> completableFuture = new CompletableFuture<>();
        TestSubscriber.Probe<Pancake> sinkProbe = new TestSubscriber.Probe<>(system);
        Sink<Pancake, CompletionStage<Done>> sink = Sink.fromSubscriber(sinkProbe)
                .mapMaterializedValue(done -> completableFuture);

        // Act
        CompletionStage<Void> runComplete = Application.runSplitter(source, sink);
        sourceProbe.sendNext(new PancakeOrder(0, PancakeAddIn.BLUEBERRY));
        sourceProbe.sendComplete();
        Pancake pancake = sinkProbe.requestNext();

        // Assert
        sinkProbe.expectComplete();
        assertEquals("BlueberryPancake(0)", pancake.toString());

        completableFuture.complete(Done.getInstance());
        assertTrue(runComplete.toCompletableFuture().isDone());
    }

    @Test
    void giveAListOfDifferentPancakeOrders_whenRunSplitter_thenReturnPancakesInAnyOrderSent() {
        // Arrange
        ActorSystem system = ActorSystem.create("splitter-test");

        TestPublisher.Probe<PancakeOrder> sourceProbe = new TestPublisher.Probe<>(1, system);
        Source<PancakeOrder, NotUsed> source = Source.fromPublisher(sourceProbe);

        CompletableFuture<Done> completableFuture = new CompletableFuture<>();
        TestSubscriber.Probe<Pancake> sinkProbe = new TestSubscriber.Probe<>(system);
        Sink<Pancake, CompletionStage<Done>> sink = Sink.fromSubscriber(sinkProbe)
                .mapMaterializedValue(done -> completableFuture);

        // Act
        CompletionStage<Void> runComplete = Application.runSplitter(source, sink);
        sourceProbe.sendNext(new PancakeOrder(0, PancakeAddIn.BLUEBERRY));
        sourceProbe.sendNext(new PancakeOrder(3, PancakeAddIn.CHOCOLATE_CHIP));
        sourceProbe.sendNext(new PancakeOrder(2, PancakeAddIn.BANANA));
        sourceProbe.sendNext(new PancakeOrder(1, PancakeAddIn.BLUEBERRY));
        sourceProbe.sendComplete();
        Pancake pancake0 = sinkProbe.requestNext();
        Pancake pancake1 = sinkProbe.requestNext();
        Pancake pancake2 = sinkProbe.requestNext();
        Pancake pancake3 = sinkProbe.requestNext();

        // Assert
        sinkProbe.expectComplete();
        List<String> pancakes =
                List.of(pancake0.toString(), pancake1.toString(), pancake2.toString(), pancake3.toString());
        assertThat("Pancakes are returned without order",
                pancakes,
                containsInAnyOrder(
                        "BlueberryPancake(0)",
                        "ChocolateChipPancake(3)",
                        "BananaPancake(2)",
                        "BlueberryPancake(1)"));

        completableFuture.complete(Done.getInstance());
        assertTrue(runComplete.toCompletableFuture().isDone());
    }

    @Test
    void givenABlueberryPancakeOrder_whenRunSplitterBatch_thenReturnBlueberryPancake() {
        // Arrange
        ActorSystem system = ActorSystem.create("splitter-test");

        TestPublisher.Probe<Ticket> sourceProbe = new TestPublisher.Probe<>(1, system);
        Source<Ticket, NotUsed> source = Source.fromPublisher(sourceProbe);

        CompletableFuture<Done> completableFuture = new CompletableFuture<>();
        TestSubscriber.Probe<PancakeBatch> sinkProbe = new TestSubscriber.Probe<>(system);
        Sink<PancakeBatch, CompletionStage<Done>> sink = Sink.fromSubscriber(sinkProbe)
                .mapMaterializedValue(done -> completableFuture);

        // Act
        CompletionStage<Void> runComplete = Application.runBatchSplitter(source, sink);
        sourceProbe.sendNext(new Ticket(0, List.of(new PancakeOrder(0, PancakeAddIn.BLUEBERRY))));
        sourceProbe.sendComplete();
        PancakeBatch pancake = sinkProbe.requestNext();

        // Assert
        sinkProbe.expectComplete();
        assertEquals("PancakeBatch [ticketId=0, pancakes=[BlueberryPancake(0)]]", pancake.toString());

        completableFuture.complete(Done.getInstance());
        assertTrue(runComplete.toCompletableFuture().isDone());
    }

    @Test
    void giveAListOfDifferentPancakeOrders_whenRunSplitterBatch_thenReturnPancakeBatchesInAnyOrderSent() {
        // Arrange
        ActorSystem system = ActorSystem.create("splitter-test");

        TestPublisher.Probe<Ticket> sourceProbe = new TestPublisher.Probe<>(1, system);
        Source<Ticket, NotUsed> source = Source.fromPublisher(sourceProbe);

        CompletableFuture<Done> completableFuture = new CompletableFuture<>();
        TestSubscriber.Probe<PancakeBatch> sinkProbe = new TestSubscriber.Probe<>(system);
        Sink<PancakeBatch, CompletionStage<Done>> sink = Sink.fromSubscriber(sinkProbe)
                .mapMaterializedValue(done -> completableFuture);

        // Act
        CompletionStage<Void> runComplete = Application.runBatchSplitter(source, sink);
        sourceProbe.sendNext(new Ticket(0, List.of(new PancakeOrder(0, PancakeAddIn.BLUEBERRY))));
        sourceProbe.sendNext(new Ticket(1, List.of(new PancakeOrder(1, PancakeAddIn.CHOCOLATE_CHIP))));
        sourceProbe.sendNext(new Ticket(2, List.of(new PancakeOrder(2, PancakeAddIn.BANANA))));
        sourceProbe.sendNext(new Ticket(3, List.of(new PancakeOrder(3, PancakeAddIn.BLUEBERRY))));
        sourceProbe.sendComplete();
        PancakeBatch pancake0 = sinkProbe.requestNext();
        PancakeBatch pancake1 = sinkProbe.requestNext();
        PancakeBatch pancake2 = sinkProbe.requestNext();
        PancakeBatch pancake3 = sinkProbe.requestNext();

        // Assert
        sinkProbe.expectComplete();
        List<String> pancakes =
                List.of(pancake0.toString(), pancake1.toString(), pancake2.toString(), pancake3.toString());
        assertThat("Pancakes are returned without order",
                pancakes,
                containsInAnyOrder(
                        "PancakeBatch [ticketId=0, pancakes=[BlueberryPancake(0)]]",
                        "PancakeBatch [ticketId=1, pancakes=[ChocolateChipPancake(1)]]",
                        "PancakeBatch [ticketId=2, pancakes=[BananaPancake(2)]]",
                        "PancakeBatch [ticketId=3, pancakes=[BlueberryPancake(3)]]"));

        completableFuture.complete(Done.getInstance());
        assertTrue(runComplete.toCompletableFuture().isDone());
    }
}
