package com.adobe.akka.stream.splitter.types;

import java.util.List;

public class Ticket {
    List<PancakeOrder> pancakeOrders;
    int id;

    public Ticket(int id, List<PancakeOrder> pancakeOrders) {
        this.pancakeOrders = pancakeOrders;
        this.id = id;
    }

    public List<PancakeOrder> getPancakeOrders() {
        return pancakeOrders;
    }

    public int getId() {
        return id;
    }

    public String toString() {
        return "Ticket " + id + " " + pancakeOrders;
    }
}
