package ru.javawebinar.topjava.repository.inmemory;

import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.repository.MealRepository;
import ru.javawebinar.topjava.util.MealsUtil;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

public class InMemoryMealRepositoryImpl implements MealRepository {
    private Map<Key, Meal> repo = new ConcurrentHashMap<>();
    private AtomicInteger counter = new AtomicInteger(0);

    class Key {
        private int id;
        private int userId;

        public Key(int id, int userId) {
            this.id = id;
            this.userId = userId;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Key key = (Key) o;
            return id == key.id &&
                    userId == key.userId;
        }

        @Override
        public int hashCode() {
            return Objects.hash(id, userId);
        }

        @Override
        public String toString() {
            return "Key{" +
                    "id=" + id +
                    ", userId=" + userId +
                    '}';
        }
    }

    {
        MealsUtil.MEALS.forEach(meal -> this.save(meal, 0));
    }

    @Override
    public Meal save(Meal meal, int userId) {
        if (meal.isNew()) {
            meal.setId(counter.incrementAndGet());
            repo.put(new Key(meal.getId(), userId), meal);
            return meal;
        }
        // treat case: update, but absent in storage
        Key first = getKey(meal.getId(), userId);
        return repo.computeIfPresent(first, (key, oldMeal) -> meal);
    }

    @Override
    public boolean delete(int id, int userId) {
        Key first = getKey(id, userId);
        if (first == null) {
            return false;
        }
        return repo.remove(first) != null;
    }

    @Override
    public Meal get(int id, int userId) {
        Key first = getKey(id, userId);
        if (first == null) {
            return null;
        }
        return repo.get(first);
    }

    @Override
    public Collection<Meal> getAll(int userId) {
        return repo.keySet().stream().
                filter(key -> key.userId == userId).
                map(key -> repo.get(key)).
                sorted(Comparator.comparing(Meal::getDate).
                        reversed()).
                collect(Collectors.toCollection(ArrayList::new));
    }

    private Key getKey(int id, int userId) {
        return repo.keySet().stream().
                filter(key -> key.id == id && key.userId == userId).
                findFirst().orElse(null);
    }
}

