package com.adobe.akka.stream.splitter.types;

public class Pancake {
    private final int index;

    public Pancake(int index) {
        this.index = index;
    }

    @Override
    public String toString() {
        return "Pancake(" + index + ")";
    }
}