package com.airtribe.ridewise.util;

import java.util.concurrent.atomic.AtomicInteger;

public class IdGenerator {
    private static final AtomicInteger counter = new AtomicInteger(0);

    private IdGenerator() {
        // utility class, should not be instantiated
    }

    public static String nextId(String prefix) {
        return prefix + "-" + counter.incrementAndGet();
    }
}