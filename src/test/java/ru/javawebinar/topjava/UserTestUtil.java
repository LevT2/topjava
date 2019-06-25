package ru.javawebinar.topjava;

import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;

public class UserTestUtil {

    public static <T> void assertMatch(T actual, T expected) {
        assertThat(actual).isEqualToIgnoringGivenFields(expected, "registered", "roles");
    }

    public static <T> void assertMatch(Iterable<T> actual, T... expected) {
        assertMatch(actual, Arrays.asList(expected));
    }

    public static <T> void assertMatch(Iterable<T> actual, Iterable<T> expected) {
        assertThat(actual).usingElementComparatorIgnoringFields("registered", "roles").isEqualTo(expected);
    }

}
