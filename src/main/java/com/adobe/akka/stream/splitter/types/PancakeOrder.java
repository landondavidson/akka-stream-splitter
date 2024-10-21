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

    public ScoopOfBatter getScoop() {
        switch (addIn) {
            case CHOCOLATE_CHIP:
                return new ScoopOfChocolateChipBatter(index);
            case BANANA:
                return new ScoopOfBananaBatter(index);
            case BLUEBERRY:
                return new ScoopOfBlueberryBatter(index);
            default:
                throw new IllegalArgumentException("Unknown addIn: " + addIn);
        }
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
