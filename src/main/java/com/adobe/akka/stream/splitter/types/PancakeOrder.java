package com.adobe.akka.stream.splitter.types;

public class PancakeOrder {
    private final PancakeAddIn addIn;
    private final int index;

    public PancakeOrder(int index, PancakeAddIn addIn) {
        this.addIn = addIn;
        this.index = index;
    }

    public PancakeAddIn getAddIn() {
        return addIn;
    }

    public int getIndex() {
        return index;
    }

    @Override
    public String toString() {
        return "PancakeOrder(" +
                "addIn=" + addIn +
                ", index=" + index +
                ')';
    }
}
