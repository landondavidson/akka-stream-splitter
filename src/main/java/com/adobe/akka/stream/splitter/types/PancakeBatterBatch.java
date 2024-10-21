package com.adobe.akka.stream.splitter.types;

import java.util.ArrayList;
import java.util.List;

public class PancakeBatterBatch {
    private int ticketId;
    private final List<ScoopOfBatter> scoops;

    public PancakeBatterBatch(){
        scoops = new ArrayList<>();
    }

    public PancakeBatterBatch(int ticketId, List<ScoopOfBatter> scoops) {
        this.ticketId = ticketId;
        this.scoops = scoops;
    }

    public void setTicketId(int ticketId) {
        this.ticketId = ticketId;
    }
    public void concatenateScoops(List<ScoopOfBatter> scoops) {
        this.scoops.addAll(scoops);
    }

    public int getTicketId() {
        return ticketId;
    }

    public List<ScoopOfBatter> getScoops() {
        return scoops;
    }

    @Override
    public String toString() {
        return "PancakeBatterBatch{" +
                "ticketId=" + ticketId +
                ", scoops=" + scoops +
                '}';
    }
}
