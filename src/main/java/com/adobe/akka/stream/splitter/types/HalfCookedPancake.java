package com.adobe.akka.stream.splitter.types;

public class HalfCookedPancake {
    protected final int index;

    public HalfCookedPancake(int index) {
        this.index = index;
    }

    public static HalfCookedPancake fromBatter(ScoopOfBatter batter) {
        return batter.cook();
    }

    public Pancake complete() {
        return new Pancake(this.index);
    }

    @Override
    public String toString() {
        return "HalfCookedPancake(" + index + ")";
    }
}
