package ru.javawebinar.topjava;

import ru.javawebinar.topjava.model.Meal;

import java.util.Arrays;

import static org.junit.Assert.assertEquals;

public class MealTestUtil {


    public static <T> void assertMatch(Iterable<T> actual, T... expected) {
        assertMatch(actual, Arrays.asList(expected));
    }

    public static <T> void assertMatch(Iterable<T> actual, Iterable<T> expected) {
        assertEquals(expected.toString(), actual.toString());
    }

//    public static <T> void assertMatch(T actual, T expected) {
//        assertEquals(expected.toString(), actual.toString());
//    }
}