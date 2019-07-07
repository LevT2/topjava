package ru.javawebinar.topjava.repository.datajpa;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ru.javawebinar.topjava.model.Meal;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Transactional(readOnly = true)
public interface CrudMealRepository extends JpaRepository<Meal, Integer> {

    @Transactional
    @Modifying
    @Query(name = Meal.DELETE)
    int deleteByIdAndUserId(@Param("id") Integer id, @Param("userId") Integer userId);

    @Query("SELECT m FROM Meal m WHERE m.id = :id AND m.user.id = :userId")
    Optional<Meal> findByIdAndUserId(@Param("id") Integer id, @Param("userId") Integer userId);

    @Transactional(propagation = Propagation.SUPPORTS)
    @Query("SELECT m FROM Meal m WHERE m.id = :id AND m.user.id = :userId")
    Optional<Meal> findByIdAndUserId_InTransaction(@Param("id") Integer id, @Param("userId") Integer userId);

    @Query("SELECT m FROM Meal m WHERE m.user.id = :userId")
    List<Meal> findByUserId(@Param("userId") Integer userId, Sort sort);

    @Query(name = Meal.GET_BETWEEN)
    List<Meal> findBetween(@Param("userId") Integer userId, @Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate);
}
