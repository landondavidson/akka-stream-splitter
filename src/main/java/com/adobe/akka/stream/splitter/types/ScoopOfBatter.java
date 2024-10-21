package com.adobe.akka.stream.splitter.types;

public class ScoopOfBatter {
    private final int index;

    public ScoopOfBatter(int index) {
        this.index = index;
    }

    public int getIndex() {
        return index;
    }

    @Override
    public String toString() {
        return "ScoopOfBatter(" + index + ")";
    }
}
