package com.adobe.akka.stream.splitter.types;

public class ScoopOfChocolateChipBatter extends ScoopOfBatter {
    public ScoopOfChocolateChipBatter(int index) {
        super(index);
    }

    @Override
    public HalfCookedPancake cook() {
        return new HalfCookedChocolateChipPancake(getIndex());
    }

    @Override
    public String toString() {
        return "ScoopOfChocolateChipBatter(" + getIndex() + ")";
    }
}
