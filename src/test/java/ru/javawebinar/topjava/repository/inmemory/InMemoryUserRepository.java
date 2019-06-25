package ru.javawebinar.topjava.repository.inmemory;

import org.springframework.stereotype.Repository;
import ru.javawebinar.topjava.TestData;
import ru.javawebinar.topjava.model.User;
import ru.javawebinar.topjava.repository.UserRepository;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import static ru.javawebinar.topjava.TestData.ADMIN;
import static ru.javawebinar.topjava.TestData.USER;


@Repository
public class InMemoryUserRepository extends InMemoryBaseRepository<User> implements UserRepository {

    static final int USER_ID = 1;
    static final int ADMIN_ID = 2;

    public void init() {
        entryMap.clear();
        entryMap.put(TestData.USER_ID, USER);
        entryMap.put(TestData.ADMIN_ID, ADMIN);
    }

    @Override
    public List<User> getAll() {
        return getCollection().stream()
                .sorted(Comparator.comparing(User::getName).thenComparing(User::getEmail))
                .collect(Collectors.toList());
    }

    @Override
    public User getByEmail(String email) {
        return getCollection().stream()
                .filter(u -> email.equals(u.getEmail()))
                .findFirst()
                .orElse(null);
    }
}