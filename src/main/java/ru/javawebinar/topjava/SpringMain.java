package ru.javawebinar.topjava;

import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.model.Role;
import ru.javawebinar.topjava.model.User;
import ru.javawebinar.topjava.util.MealsUtil;
import ru.javawebinar.topjava.web.meal.MealRestController;
import ru.javawebinar.topjava.web.user.AdminRestController;

import java.util.Arrays;
import java.util.Collection;

public class SpringMain {
    public static void main(String[] args) {
        // java 7 automatic resource management
        try (ConfigurableApplicationContext appCtx = new ClassPathXmlApplicationContext("spring/spring-app.xml")) {
            System.out.println("Bean definition names: " + Arrays.toString(appCtx.getBeanDefinitionNames()));
            AdminRestController adminUserController = appCtx.getBean(AdminRestController.class);
            adminUserController.create(new User(null, "userName", "email@mail.ru", "password", Role.ROLE_ADMIN));

            MealRestController controller = appCtx.getBean(MealRestController.class);


            MealsUtil.MEALS.forEach(meal -> {
                Meal newMeal = new Meal(meal.getDateTime(), "!!!" + meal.getDescription(), meal.getCalories());
                controller.create(newMeal);
            });

            controller.getAllTo().forEach(SpringMain::printAll);


            Meal meal = MealsUtil.MEALS.get(0);
            Meal newMeal = new Meal(meal.getDateTime(), meal.getDescription() + "!!!", meal.getCalories());
            newMeal.setId(1);

            controller.update(newMeal,1);

            controller.getAllTo().forEach(SpringMain::printAll);

        }
    }

    private static <T> void printAll(T item) {
        System.out.println(item.toString());
    }
}
