package ru.vorobyev.tracker.repository.inmemory;

import java.util.concurrent.atomic.AtomicInteger;

public class SeqGenerator {

    public static final AtomicInteger BUG_SEQ_GENERATOR = new AtomicInteger(100);
    public static final AtomicInteger EPIC_SEQ_GENERATOR = new AtomicInteger(100);
    public static final AtomicInteger STORY_SEQ_GENERATOR = new AtomicInteger(100);
    public static final AtomicInteger TASK_SEQ_GENERATOR = new AtomicInteger(100);

    private SeqGenerator() {
    }
}
