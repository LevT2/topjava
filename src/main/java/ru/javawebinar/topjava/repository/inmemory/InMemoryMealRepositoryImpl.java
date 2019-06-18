package ru.javawebinar.topjava.repository.inmemory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.repository.MealRepository;
import ru.javawebinar.topjava.util.DateTimeUtil;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Repository
public class InMemoryMealRepositoryImpl implements MealRepository {
    private static final Logger LOG = LoggerFactory.getLogger(InMemoryMealRepositoryImpl.class);
    private AtomicInteger counter = new AtomicInteger(0);
    private Map<Integer, Map<Integer, Meal>> repo = new ConcurrentHashMap<>();


    @Override
    public Meal save(Meal meal, int userId) {
        Map<Integer, Meal> mealMap = repo.computeIfAbsent(userId, ConcurrentHashMap::new);

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
            return meal;
        }
    }

    @Override
    public boolean delete(int id, int userId) {
        Meal delete = get(id, userId);
        if (delete == null) {
            LOG.error("Invalid attempt to delete meal, id:{}", id);
            return false;
        }
        Meal remove = repo.get(userId).remove(delete.getId());
        return remove != null;
    }

    @Override
    public Meal get(int id, int userId) {
        Map<Integer, Meal> userMeals = repo.get(userId);
        return (userMeals == null) ? null : userMeals.get(id);
    }

    @Override
    public Collection<Meal> getAll(int userId) {
        return getAllWhere(userId, meal -> true);
    }

    @Override
    public Collection<Meal> getAllBetween(LocalDate startTime, LocalDate endTime, int userId) {
        return getAllWhere(userId, meal -> DateTimeUtil.isBetween(meal.getDate(), startTime, endTime));
    }

    private Collection<Meal> getAllWhere(int userId, Predicate<Meal> filter) {
        return repo.getOrDefault(userId, new ConcurrentHashMap<>()).values().stream().
                filter(filter).
                sorted(Comparator.comparing(Meal::getDate).
                        reversed()).
                collect(Collectors.toCollection(ArrayList::new));
    }
}
