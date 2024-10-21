package com.adobe.akka.stream.splitter.types;

public class HalfCookedPancake {
    private final int index;

    public HalfCookedPancake(int index) {
        this.index = index;
    }

    public static HalfCookedPancake fromBatter(ScoopOfBatter batter) {
        return new HalfCookedPancake(batter.getIndex()) {
        };
    }

    public Pancake complete() {
        return new Pancake(this.index);
    }

    @Override
    public String toString() {
        return "HalfCookedPancake(" + index + ")";
    }
}
