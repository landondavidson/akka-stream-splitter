package com.adobe.akka.stream.splitter.types;

public class HalfCookedChocolateChipPancake extends HalfCookedPancake {
    public HalfCookedChocolateChipPancake(int index) {
        super(index);
    }

    @Override
    public Pancake complete() {
        return new ChocolateChipPancake(index);
    }

    @Override
    public String toString() {
        return "HalfCookedChocolateChipPancake(" + index + ")";
    }
}
