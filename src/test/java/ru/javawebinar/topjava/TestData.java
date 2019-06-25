package ru.javawebinar.topjava;

import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.model.Role;
import ru.javawebinar.topjava.model.User;

import java.time.LocalDateTime;
import java.time.Month;

import static ru.javawebinar.topjava.model.AbstractBaseEntity.START_SEQ;

public class TestData {
    public static final int USER_ID = START_SEQ;
    public static final User USER = new User(USER_ID, "User", "user@yandex.ru", "password", Role.ROLE_USER);
    public static final int ADMIN_ID = 100001;
    public static final User ADMIN = new User(ADMIN_ID, "Admin", "admin@gmail.com", "admin", Role.ROLE_ADMIN);
    public static final int MEAL_ID2 = START_SEQ + 2;
    public static final Meal MEAL2 = new Meal(MEAL_ID2, LocalDateTime.of(2015, Month.MAY, 30, 10, 0), "Завтрак", 500);
    public static final int MEAL_ID3 = START_SEQ + 3;
    public static final Meal MEAL3 = new Meal(MEAL_ID3, LocalDateTime.of(2015, Month.MAY, 30, 13, 0), "Обед", 1000);
    public static final int MEAL_ID4 = START_SEQ + 4;
    public static final Meal MEAL4 = new Meal(MEAL_ID4, LocalDateTime.of(2015, Month.MAY, 30, 20, 0), "Ужин", 500);
    public static final int MEAL_ID5 = START_SEQ + 5;
    public static final Meal MEAL5 = new Meal(MEAL_ID5, LocalDateTime.of(2015, Month.MAY, 31, 10, 0), "Завтрак", 1000);
    public static final int MEAL_ID6 = START_SEQ + 6;
    public static final Meal MEAL6 = new Meal(MEAL_ID6, LocalDateTime.of(2015, Month.MAY, 31, 13, 0), "Обед", 500);
    public static final int MEAL_ID7 = START_SEQ + 7;
    public static final Meal MEAL7 = new Meal(MEAL_ID7, LocalDateTime.of(2015, Month.MAY, 31, 20, 0), "Ужин", 510);
    public static final int MEAL_ID8 = START_SEQ + 8;
    public static final Meal MEAL8 = new Meal(MEAL_ID8, LocalDateTime.of(2015, Month.JUNE, 30, 8, 0), "Завтрак", 400);
    public static final int MEAL_ID9 = START_SEQ + 9;
    public static final Meal MEAL9 = new Meal(MEAL_ID9, LocalDateTime.of(2015, Month.JUNE, 30, 12, 0), "Обед", 900);
    public static final int MEAL_ID10 = START_SEQ + 10;
    public static final Meal MEAL10 = new Meal(MEAL_ID10, LocalDateTime.of(2015, Month.JUNE, 30, 18, 0), "Ужин", 750);
}
