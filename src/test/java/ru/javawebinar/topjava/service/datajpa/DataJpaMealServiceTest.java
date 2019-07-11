package ru.javawebinar.topjava.service.datajpa;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import ru.javawebinar.topjava.Profiles;
import ru.javawebinar.topjava.UserTestData;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.service.AbstractMealServiceTest;
import ru.javawebinar.topjava.service.UserService;

import static org.assertj.core.api.Assertions.assertThat;
import static ru.javawebinar.topjava.MealTestData.MEAL1_ID;
import static ru.javawebinar.topjava.MealTestData.assertMatch;
import static ru.javawebinar.topjava.UserTestData.USER_ID;

@ActiveProfiles(Profiles.DATAJPA)
public class DataJpaMealServiceTest extends AbstractMealServiceTest {

    @Autowired
    protected UserService userService;

    @Test
    public void getWithUser_returnsProperUser() throws Exception {
        Meal meal = service.getWithUser(MEAL1_ID);
        UserTestData.assertMatch(meal.getUser(), userService.get(USER_ID));
    }

    @Test
    public void getWithUser_returnsProperMeal() throws Exception {
        Meal meal = service.getWithUser(MEAL1_ID);
        assertMatch(meal, service.get(MEAL1_ID, USER_ID));
    }

    @Test
    public void getWithWser_MealNotExists() throws Exception {
        Meal meal = service.getWithUser(1);
        assertThat(meal).isNull();
    }
}
