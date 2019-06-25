package ru.javawebinar.topjava.service;

import org.junit.AfterClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.bridge.SLF4JBridgeHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlConfig;
import org.springframework.test.context.junit4.SpringRunner;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.util.exception.NotFoundException;

import java.time.LocalDateTime;
import java.time.Month;

import static org.junit.Assert.assertEquals;
import static ru.javawebinar.topjava.MealTestData.*;
import static ru.javawebinar.topjava.UserTestData.ADMIN_ID;
import static ru.javawebinar.topjava.UserTestData.USER_ID;

@ContextConfiguration({
        "classpath:spring/spring-test-db.xml",
        "classpath:spring/spring-db.xml"
})
@RunWith(SpringRunner.class)
@Sql(scripts = "classpath:db/populateDB.sql", config = @SqlConfig(encoding = "UTF-8"))
public class MealServiceTest {

    static {
        // Only for postgres driver logging
        // It uses java.util.logging and logged via jul-to-slf4j bridge
        SLF4JBridgeHandler.install();
    }

    @AfterClass
    public static void bridge() {
        SLF4JBridgeHandler.uninstall();
    }

    @Autowired
    private MealService service;

    @Test
    public void get() {
        Meal meal = service.get(MEAL_ID9, ADMIN_ID);
        assertMatch(service.getAll(ADMIN_ID), MEAL10, meal, MEAL8);
    }

    @Test
    public void delete() {
        service.delete(MEAL_ID2, USER_ID);
        assertMatch(service.getAll(USER_ID), MEAL7, MEAL6, MEAL5, MEAL4, MEAL3);
    }

    @Test
    public void getBetweenDateTimes() {
        assertMatch(service.getBetweenDateTimes(LocalDateTime.of(2015, Month.MAY, 30, 13, 0), LocalDateTime.of(2015, Month.MAY, 30, 13, 0), USER_ID), MEAL3);
    }

    @Test
    public void getAll() {
        assertMatch(service.getAll(USER_ID), MEAL7, MEAL6, MEAL5, MEAL4, MEAL3, MEAL2);
    }

    @Test
    public void update() {
        Meal updatedMeal = new Meal(MEAL_ID4, LocalDateTime.of(2015, Month.MAY, 30, 17, 5), "Полдник", 250);
        service.update(updatedMeal, USER_ID);
        assertEquals(service.get(MEAL_ID4, USER_ID).toString(), updatedMeal.toString());
    }

    @Test
    public void create() {
        Meal newMeal = new Meal(LocalDateTime.of(2015, Month.MAY, 31, 17, 5), "Полдник", 250);
        service.create(newMeal, USER_ID);
        assertMatch(service.getAll(USER_ID), MEAL7, newMeal, MEAL6, MEAL5, MEAL4, MEAL3, MEAL2);
    }

    @Test(expected = NotFoundException.class)
    public void deleteForeign() {
        service.delete(MEAL_ID2, ADMIN_ID);
    }

    @Test(expected = NotFoundException.class)
    public void getForeign() {
        service.get(MEAL_ID2, ADMIN_ID);
    }

    @Test(expected = NotFoundException.class)
    public void updateForeign() {
        service.update(MEAL4, ADMIN_ID);
    }

    @Test(expected = DuplicateKeyException.class)
    public void createDuplicate() {
        Meal newMeal = new Meal(LocalDateTime.of(2015, Month.MAY, 30, 13, 0), "Обед", 1000);
        service.create(newMeal, USER_ID);
        assertMatch(service.getAll(USER_ID), MEAL7, newMeal, MEAL6, MEAL5, MEAL4, MEAL3, MEAL2);
    }
}