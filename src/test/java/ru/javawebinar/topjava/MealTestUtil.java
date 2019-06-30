package ru.javawebinar.topjava;

import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;

public class MealTestUtil {

    public static <Meal> void assertMatch(Iterable<Meal> actual, Meal... expected) {
        assertMatch(actual, Arrays.asList(expected));
    }

    public static <Meal> void assertMatch(Iterable<Meal> actual, Iterable<Meal> expected) {
        assertThat(actual).as("List Assert - equals").containsSequence(expected);
    }
}