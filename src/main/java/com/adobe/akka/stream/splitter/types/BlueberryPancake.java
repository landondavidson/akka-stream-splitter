package com.adobe.akka.stream.splitter.types;

public class BlueberryPancake extends Pancake {
    public BlueberryPancake(int index) {
        super(index);
    }

    @Override
    public String toString() {
        return "BlueberryPancake(" + index + ")";
    }
}
