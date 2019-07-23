package ru.javawebinar.topjava.repository.jdbc;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.support.DataAccessUtils;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.javawebinar.topjava.model.Role;
import ru.javawebinar.topjava.model.User;
import ru.javawebinar.topjava.repository.UserRepository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import java.util.stream.Collectors;

@Transactional(readOnly = true)
@Repository
public class JdbcUserRepository implements UserRepository {
    protected final Logger log = LoggerFactory.getLogger(getClass());

    private static final BeanPropertyRowMapper<User> ROW_MAPPER = BeanPropertyRowMapper.newInstance(User.class);

    private final JdbcTemplate jdbcTemplate;

    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    private final SimpleJdbcInsert insertUser;

    @Autowired
    public JdbcUserRepository(JdbcTemplate jdbcTemplate, NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        this.insertUser = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("users")
                .usingGeneratedKeyColumns("id");

        this.jdbcTemplate = jdbcTemplate;
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
    }

    @Override
    public User save(User user) {
        BeanPropertySqlParameterSource parameterSource = new BeanPropertySqlParameterSource(user);

        if (user.isNew()) {
            Number newKey = insertUser.executeAndReturnKey(parameterSource);
            user.setId(newKey.intValue());
        } else if (namedParameterJdbcTemplate.update(
                "UPDATE users SET name=:name, email=:email, password=:password, " +
                        "registered=:registered, enabled=:enabled, calories_per_day=:caloriesPerDay WHERE id=:id", parameterSource) == 0) {
            return null;
        }
        return user;
    }

    @Override
    @Transactional
    public boolean delete(int id) {
        return jdbcTemplate.update("DELETE FROM users WHERE id=?", id) != 0;
    }

    @Override
    public User get(int id) {
        List<User> users = jdbcTemplate.query("SELECT * FROM users WHERE id=?", ROW_MAPPER, id);
        users.forEach(this::hydrateRoles);
        return DataAccessUtils.singleResult(users);
    }

    @Override
    public User getByEmail(String email) {
        List<User> users = jdbcTemplate.query("SELECT * FROM users WHERE email=?", ROW_MAPPER, email);
        users.forEach(this::hydrateRoles);
        return DataAccessUtils.singleResult(users);
    }

    @Override
    public List<User> getAll() {
        String query = "SELECT user_id, role, id , name, email, password, registered, enabled, calories_per_day " +
                "FROM user_roles RIGHT JOIN users AS u on user_roles.user_id = u.id ORDER BY u.name, u.email";

        return jdbcTemplate.query(query, rs -> {
            Map<User, Set<Role>> map = new HashMap<>();

            while (rs.next()) {
                User user = new User(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("email"),
                        rs.getString("password"),
                        rs.getInt("calories_per_day"),
                        rs.getBoolean("enabled"),
                        rs.getDate("registered"),
                        new HashSet<>()
                );

                var userRoles = map.computeIfAbsent(user, role -> new HashSet<>());
                userRoles.add(Role.valueOf(rs.getString("role")));
            }

            return map.entrySet().stream().
                    map(JdbcUserRepository::hydrateRoles).
                    collect(Collectors.toList());
        });
    }


    private static User hydrateRoles(Map.Entry<User, Set<Role>> userSetEntry) {
        User bareUser = userSetEntry.getKey();
        return new User(
                bareUser.getId(),
                bareUser.getName(),
                bareUser.getEmail(),
                bareUser.getPassword(),
                bareUser.getCaloriesPerDay(),
                bareUser.isEnabled(),
                bareUser.getRegistered(),
                userSetEntry.getValue()
        );
    }

    private void hydrateRoles(User user) {
        user.setRoles(getRoles(user.getId()));
    }

    private Collection<Role> getRoles(int userId) {
        return jdbcTemplate.query("SELECT * FROM user_roles WHERE user_id = ?", JdbcUserRepository::mapUserRoles, userId);
    }

    private static Role mapUserRoles(ResultSet rs, int rowNum) throws SQLException {
        String roleName = rs.getString("role");
        return Role.valueOf(roleName);
    }
}
