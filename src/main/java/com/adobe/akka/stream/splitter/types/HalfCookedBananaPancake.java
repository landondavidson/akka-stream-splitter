package com.adobe.akka.stream.splitter.types;

public class HalfCookedBananaPancake extends HalfCookedPancake {
    public HalfCookedBananaPancake(int index) {
        super(index);
    }

    @Override
    public Pancake complete() {
        return new BananaPancake(index);
    }

    @Override
    public String toString() {
        return "HalfCookedBananaPancake(" + index + ")";
    }
}
