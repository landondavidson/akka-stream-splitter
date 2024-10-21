package com.adobe.akka.stream.splitter.types;

public class BananaPancake extends Pancake {
    public BananaPancake(int index) {
        super(index);
    }

    @Override
    public String toString() {
        return "BananaPancake(" + index + ")";
    }
}
