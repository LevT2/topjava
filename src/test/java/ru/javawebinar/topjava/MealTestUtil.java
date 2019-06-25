package ru.javawebinar.topjava;

import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;

public class MealTestUtil {

    public static <T> void assertMatch(Iterable<T> actual, T... expected) {
        assertMatch(actual, Arrays.asList(expected));
    }

    public static <T> void assertMatch(Iterable<T> actual, Iterable<T> expected) {
        assertThat(actual).as("List Assert - equals").containsSequence(expected);
    }
}