package com.adobe.akka.stream.splitter.types;

public class HalfCookedBlueberryPancake extends HalfCookedPancake {
    public HalfCookedBlueberryPancake(int index) {
        super(index);
    }

    @Override
    public Pancake complete() {
        return new BlueberryPancake(index);
    }

    @Override
    public String toString() {
        return "HalfCookedBlueberryPancake(" + index + ")";
    }
}
