package ru.javawebinar.topjava.repository.inmemory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.repository.MealRepository;
import ru.javawebinar.topjava.util.MealsUtil;
import ru.javawebinar.topjava.web.SecurityUtil;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

public class InMemoryMealRepositoryImpl implements MealRepository {
    private static final Logger LOG = LoggerFactory.getLogger(InMemoryMealRepositoryImpl.class);

    private Map<Integer, Meal> repository = new ConcurrentHashMap<>();
    private AtomicInteger counter = new AtomicInteger(0);

    private Map<Integer, List<Integer>> userMeals = new HashMap<>();

    {
        MealsUtil.MEALS.forEach(meal -> this.save(meal, SecurityUtil.authUserId()));
        userMeals.put(SecurityUtil.authUserId(), MealsUtil.MEALS.stream().
                map(meal -> meal.getId()).
                collect(Collectors.toList()));
    }

    @Override
    public Meal save(Meal meal, int userId) {
        List<Integer> currentUserMeals = userMeals.get(userId);

        if (meal.isNew()) {
            meal.setId(counter.incrementAndGet());
            repository.put(meal.getId(), meal);
            currentUserMeals.add(meal.getId());
            return meal;
        } else {
            if (!currentUserMeals.contains(meal.getId())) {
                LOG.error("Attempt to modify meal of someone else, id:{}", meal.getId());
                return null;
            }
        }
        // treat case: update, but absent in storage
        return repository.computeIfPresent(meal.getId(), (id, oldMeal) -> meal);
    }

    @Override
    public boolean delete(int id, int userId) {
        List<Integer> currentUserMeals = userMeals.get(userId);

        if (currentUserMeals.contains(id)) {
            return repository.remove(id) != null;
        } else {
            LOG.error("Attempt to delete meal of someone else, id:{}, {}", id, repository.get(id).toString());
            return false;
        }
    }

    @Override
    public Meal get(int id, int userId) {
        List<Integer> currentUserMeals = userMeals.get(userId);
        if (currentUserMeals.contains(id)) {
            return repository.get(id);
        } else {
            LOG.error("Attempt to get meal of someone else, id:{}, {}", id, repository.get(id).toString());
            return null;
        }
    }

    @Override
    public Collection<Meal> getAll(int userId) {
        List<Integer> currentUserMeals = userMeals.get(userId);
        return repository.values().stream().
                filter(meal -> currentUserMeals.contains(meal.getId())).
                sorted(Comparator.comparing(Meal::getDate).
                        reversed()).
                collect(Collectors.toCollection(ArrayList::new));
    }
}

