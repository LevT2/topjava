package ru.javawebinar.topjava;

import ru.javawebinar.topjava.model.User;

import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;

public class UserTestUtil {

    public static void assertMatch(User actual, User expected) {
        assertThat(actual).isEqualToIgnoringGivenFields(expected, "registered", "roles");
    }

    public static void assertMatch(Iterable<User> actual, User... expected) {
        assertMatch(actual, Arrays.asList(expected));
    }

    public static void assertMatch(Iterable<User> actual, Iterable<User> expected) {
        assertThat(actual).usingElementComparatorIgnoringFields("registered", "roles").isEqualTo(expected);
    }
}
