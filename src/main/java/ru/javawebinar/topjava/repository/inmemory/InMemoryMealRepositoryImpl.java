package ru.javawebinar.topjava.repository.inmemory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.repository.MealRepository;
import ru.javawebinar.topjava.util.DateTimeUtil;

import java.time.LocalDate;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Repository
public class InMemoryMealRepositoryImpl implements MealRepository {
    private static final Logger LOG = LoggerFactory.getLogger(InMemoryMealRepositoryImpl.class);
    private AtomicInteger counter = new AtomicInteger(0);
    private Map<Integer, Map<Integer, Meal>> repo = new ConcurrentHashMap<>();


    @Override
    public Meal save(Meal meal, int userId) {

        Map<Integer, Meal> mealMap = repo.getOrDefault(userId, new ConcurrentHashMap<>());
        repo.put(userId, mealMap);

        if (meal.isNew()) {
            meal.setId(counter.incrementAndGet());
            mealMap.put(meal.getId(), meal);

            return meal;
        } else {
            Meal update = mealMap.get(meal.getId());
            if (update == null) {
                LOG.error("Attempt to modify meal of someone else, id:{}", meal.getId());
                return null;
            }
            // treat case: update, but absent in storage
            mealMap.compute(meal.getId(), (id, oldMeal) -> meal);
            return mealMap.get(meal.getId());
        }
    }

    @Override
    public boolean delete(int id, int userId) {

        Meal delete = this.get(id, userId);
        if (delete == null) {
            LOG.error("Invalid attempt to delete meal, id:{}", id);
            return false;
        }
        repo.get(userId).remove(delete.getId());
        return true;
    }

    @Override
    public Meal get(int id, int userId) {
        Map<Integer, Meal> userMeals = repo.get(userId);
        if (userMeals == null) return null;
        Meal meal = userMeals.get(id);
        return meal;
    }

    @Override
    public Collection<Meal> getAll(int userId) {
        return repo.getOrDefault(userId, new ConcurrentHashMap<>()).values().stream().
                sorted(Comparator.comparing(Meal::getDate).
                        reversed()).
                collect(Collectors.toCollection(ArrayList::new));
    }

    @Override
    public Collection<Meal> getAllBetween(LocalDate startTime, LocalDate endTime, int userId) {
        return getAll(userId).stream().
                filter(meal -> DateTimeUtil.isBetween(meal.getDate(), startTime, endTime)).
                collect(Collectors.toCollection(ArrayList::new));
    }

//    private List<Meal> getCurrentUserMeals(int userId) {
//        List<Meal> userMeals = new ArrayList<>();
//        for (Meal meal : repo.get(userId).values()) {
//            userMeals.add(meal);
//        }
//
//        //TODO  check where to create
//        if (userMeals == null) userMeals = new ArrayList<>();
//        return userMeals;
//    }
}

