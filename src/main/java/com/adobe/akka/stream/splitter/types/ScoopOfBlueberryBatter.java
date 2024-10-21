package com.adobe.akka.stream.splitter.types;

public class ScoopOfBlueberryBatter extends ScoopOfBatter {
    public ScoopOfBlueberryBatter(int index) {
        super(index);
    }

    @Override
    public HalfCookedPancake cook() {
        return new HalfCookedBlueberryPancake(getIndex());
    }

    @Override
    public String toString() {
        return "ScoopOfBlueberryBatter(" + getIndex() + ")";
    }
}
