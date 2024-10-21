package com.adobe.akka.stream.splitter.types;

public class ScoopOfBananaBatter extends ScoopOfBatter {
    public ScoopOfBananaBatter(int index) {
        super(index);
    }

    @Override
    public HalfCookedPancake cook() {
        return new HalfCookedBananaPancake(getIndex());
    }

    @Override
    public String toString() {
        return "ScoopOfBananaBatter(" + getIndex() + ")";
    }
}
