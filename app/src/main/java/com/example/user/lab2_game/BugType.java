package com.example.user.lab2_game;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public enum BugType {
    SPIDER,
    WASP;

    private static final List<BugType> VALUES = Collections.unmodifiableList(Arrays.asList(values()));
    private static final int SIZE = VALUES.size();
    private static final Random RANDOM = new Random();

    public static BugType randomBugType() {
        return VALUES.get(RANDOM.nextInt(SIZE));
    }
}
