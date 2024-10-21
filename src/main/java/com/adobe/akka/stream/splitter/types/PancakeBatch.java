package com.adobe.akka.stream.splitter.types;

import java.util.List;

public class PancakeBatch {
    private final int ticketId;
    private final List<Pancake> pancakes;

    public PancakeBatch(int ticketId, List<Pancake> pancakes) {
        this.ticketId = ticketId;
        this.pancakes = pancakes;
    }

    public String toString() {
        return "PancakeBatch [ticketId=" + ticketId + ", pancakes=" + pancakes + "]";
    }

}
