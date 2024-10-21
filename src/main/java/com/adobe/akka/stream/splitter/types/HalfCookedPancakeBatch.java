package com.adobe.akka.stream.splitter.types;

import java.util.List;

public class HalfCookedPancakeBatch {
    private final int ticketId;
    private final List<HalfCookedPancake> halfCookedPancakes;

    public HalfCookedPancakeBatch(int ticketId, List<HalfCookedPancake> halfCookedPancakes) {
        this.ticketId = ticketId;
        this.halfCookedPancakes = halfCookedPancakes;
    }

    public int getTicketId() {
        return ticketId;
    }

    public List<HalfCookedPancake> getHalfCookedPancakes() {
        return halfCookedPancakes;
    }
}
