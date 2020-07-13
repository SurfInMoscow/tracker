package ru.vorobyev.tracker.repository.inmemory;

import java.util.concurrent.atomic.AtomicInteger;

public class SeqGenerator {

    public static final AtomicInteger SEQ_GENERATOR = new AtomicInteger(100);

    private SeqGenerator() {
    }
}
