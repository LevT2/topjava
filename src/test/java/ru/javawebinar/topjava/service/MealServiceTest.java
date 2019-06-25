package ru.javawebinar.topjava.service;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import ru.javawebinar.topjava.TestData;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.util.exception.NotFoundException;

import java.time.LocalDateTime;
import java.time.Month;

import static org.junit.Assert.assertEquals;
import static ru.javawebinar.topjava.MealTestUtil.assertMatch;
import static ru.javawebinar.topjava.TestData.ADMIN_ID;
import static ru.javawebinar.topjava.TestData.USER_ID;

public class MealServiceTest extends ServiceTest{

    @Autowired
    private MealService service;

    @Test
    public void create() {
        Meal newMeal = new Meal(LocalDateTime.of(2015, Month.MAY, 31, 17, 5), "Полдник", 250);
        service.create(newMeal, USER_ID);
        assertMatch(service.getAll(USER_ID), TestData.MEAL7, newMeal, TestData.MEAL6, TestData.MEAL5, TestData.MEAL4, TestData.MEAL3, TestData.MEAL2);
    }

    @Test(expected = DuplicateKeyException.class)
    public void createDuplicate() {
        Meal newMeal = new Meal(LocalDateTime.of(2015, Month.MAY, 30, 13, 0), "Обед", 1000);
        service.create(newMeal, USER_ID);
        assertMatch(service.getAll(USER_ID), TestData.MEAL7, newMeal, TestData.MEAL6, TestData.MEAL5, TestData.MEAL4, TestData.MEAL3, TestData.MEAL2);
    }

    @Test
    public void update() {
        Meal updatedMeal = new Meal(TestData.MEAL_ID4, LocalDateTime.of(2015, Month.MAY, 30, 17, 5), "Полдник", 250);
        service.update(updatedMeal, USER_ID);
        assertEquals(service.get(TestData.MEAL_ID4, USER_ID).toString(), updatedMeal.toString());
    }

    @Test(expected = NotFoundException.class)
    public void updateForeign() {
        service.update(TestData.MEAL4, ADMIN_ID);
    }

    @Test
    public void delete() {
        service.delete(TestData.MEAL_ID2, USER_ID);
        assertMatch(service.getAll(USER_ID), TestData.MEAL7, TestData.MEAL6, TestData.MEAL5, TestData.MEAL4, TestData.MEAL3);
    }

    @Test(expected = NotFoundException.class)
    public void deleteForeign() {
        service.delete(TestData.MEAL_ID2, ADMIN_ID);
    }

    @Test
    public void get() {
        Meal meal = service.get(TestData.MEAL_ID9, ADMIN_ID);
        assertMatch(service.getAll(ADMIN_ID), TestData.MEAL10, meal, TestData.MEAL8);
    }

    @Test(expected = NotFoundException.class)
    public void getForeign() {
        service.get(TestData.MEAL_ID2, ADMIN_ID);
    }

    @Test
    public void getBetweenDateTimes() {
        assertMatch(service.getBetweenDateTimes(LocalDateTime.of(2015, Month.MAY, 30, 13, 0), LocalDateTime.of(2015, Month.MAY, 30, 13, 0), USER_ID), TestData.MEAL3);
    }

    @Test
    public void getAll() {
        assertMatch(service.getAll(USER_ID), TestData.MEAL7, TestData.MEAL6, TestData.MEAL5, TestData.MEAL4, TestData.MEAL3, TestData.MEAL2);
    }
}