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

        //TODO Убрать res
        List<UserMealWithExceed> res = getFilteredWithExceededStream(mealList, LocalTime.of(7, 0), LocalTime.of(12, 0), 2000);

        res.forEach(i -> System.out.println(i.toString()));

        //        .toLocalDate();
        //        .toLocalTime();
    }

    public static List<UserMealWithExceed> getFilteredWithExceeded(List<UserMeal> mealList, LocalTime startTime, LocalTime endTime, int caloriesPerDay) {
        List<UserMealWithExceed> list = new ArrayList<>();
        Map<LocalDate, Integer> map = new HashMap<>();

        for (UserMeal meal : mealList) {
            LocalDate localDate = meal.getDateTime().toLocalDate();
            map.merge(localDate, meal.getCalories(), Integer::sum);
        }

        for (UserMeal meal : mealList) {
            boolean exceed;
            LocalDateTime dateTime = meal.getDateTime();
            exceed = map.get(dateTime.toLocalDate()) > caloriesPerDay;

            LocalTime targetTime = dateTime.toLocalTime();
            boolean targetInZone = targetTime.isAfter(startTime) && targetTime.isBefore(endTime);

            if (targetInZone) {
                list.add(new UserMealWithExceed(dateTime, "", meal.getCalories(), exceed));
            }
        }

        return list;
    }


    public static List<UserMealWithExceed> getFilteredWithExceededStream(List<UserMeal> mealList, LocalTime startTime, LocalTime endTime, int caloriesPerDay) {
        Map<LocalDate, Integer> mapCalories = new HashMap<>();

        mealList.forEach((meal) -> {
                            LocalDate localDate = meal.getDateTime().toLocalDate();
                            mapCalories.merge(localDate, meal.getCalories(), Integer::sum);
                        }
                );

        List<UserMealWithExceed> list = mealList.stream().
                filter((meal) -> {
                    LocalTime targetTime = meal.getDateTime().toLocalTime();
                    return targetTime.isAfter(startTime) && targetTime.isBefore(endTime);
                }).
                map((meal) -> {
                    boolean exceed = mapCalories.get(meal.getDateTime().toLocalDate()) > caloriesPerDay;
                    return new UserMealWithExceed(meal.getDateTime(), "", meal.getCalories(), exceed);
                }).
                collect(Collectors.toList());

        return list;
    }

}
