package ru.javawebinar.topjava.util;

import ru.javawebinar.topjava.model.UserMeal;
import ru.javawebinar.topjava.model.UserMealWithExceed;

import java.time.*;
import java.util.*;
import java.util.stream.Collectors;

public class UserMealsUtil {
    public static void main(String[] args) {
        List<UserMeal> mealList = Arrays.asList(
                new UserMeal(LocalDateTime.of(2015, Month.MAY, 30, 10, 0), "Завтрак", 500),
                new UserMeal(LocalDateTime.of(2015, Month.MAY, 30, 13, 0), "Обед", 1000),
                new UserMeal(LocalDateTime.of(2015, Month.MAY, 30, 20, 0), "Ужин", 500),
                new UserMeal(LocalDateTime.of(2015, Month.MAY, 31, 10, 0), "Завтрак", 1000),
                new UserMeal(LocalDateTime.of(2015, Month.MAY, 31, 13, 0), "Обед", 500),
                new UserMeal(LocalDateTime.of(2015, Month.MAY, 31, 20, 0), "Ужин", 510)
        );

        List<UserMealWithExceed> res = getFilteredWithExceededStream(mealList, LocalTime.of(7, 0), LocalTime.of(12, 0), 2000);
        res.forEach(System.out::println);

        //        .toLocalDate();
        //        .toLocalTime();
    }

    public static List<UserMealWithExceed> getFilteredWithExceeded(List<UserMeal> mealList, LocalTime startTime, LocalTime endTime, int caloriesPerDay) {
        List<UserMealWithExceed> list = new ArrayList<>();
        Map<LocalDate, Integer> dailyCalories = new HashMap<>();

        for (UserMeal meal : mealList) {
            LocalDate mealDate = getLocalDate(meal);
            dailyCalories.merge(mealDate, meal.getCalories(), Integer::sum);
        }

        for (UserMeal meal : mealList) {
            boolean exceed;
            LocalDateTime mealDateTime = meal.getDateTime();
            exceed = dailyCalories.get(mealDateTime.toLocalDate()) > caloriesPerDay;

            LocalTime targetTime = mealDateTime.toLocalTime();
            if (TimeUtil.isBetween(targetTime,startTime,endTime)) {
                list.add(new UserMealWithExceed(mealDateTime, meal.getDescription(), meal.getCalories(), exceed));
            }
        }

        return list;
    }


    public static List<UserMealWithExceed> getFilteredWithExceededStream(List<UserMeal> mealList, LocalTime startTime, LocalTime endTime, int caloriesPerDay) {
        Map<LocalDate, Integer> dailyCalories;
        dailyCalories = mealList.stream().
                collect(Collectors.toMap(
                        UserMealsUtil::getLocalDate,
                        UserMeal::getCalories,
                        Integer::sum,
                        HashMap::new
                        )
                );

        return mealList.stream().
                filter(
                        (meal) -> TimeUtil.isBetween(meal.getDateTime().toLocalTime(),startTime,endTime)
                ).
                map(meal -> {
                    boolean exceed = dailyCalories.get(getLocalDate(meal)) > caloriesPerDay;
                    return new UserMealWithExceed(meal.getDateTime(), meal.getDescription(), meal.getCalories(), exceed);
                }).
                collect(Collectors.toList());
    }

    private static LocalDate getLocalDate(UserMeal meal) {
        return meal.getDateTime().toLocalDate();
    }
}
