package ru.javawebinar.topjava.web.meal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.service.MealService;
import ru.javawebinar.topjava.to.MealTo;
import ru.javawebinar.topjava.util.MealsUtil;
import ru.javawebinar.topjava.web.SecurityUtil;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Collection;
import java.util.List;

import static ru.javawebinar.topjava.util.ValidationUtil.assureIdConsistent;
import static ru.javawebinar.topjava.util.ValidationUtil.checkNew;

@Controller
public class MealRestController {
    protected final Logger log = LoggerFactory.getLogger(getClass());

    @Autowired
    protected MealService service;

    @Autowired
    public MealRestController(MealService service) {
        this.service = service;
    }


    public List<MealTo> getAllTo() {
        log.info("getAllTo");
        Collection<Meal> mealCollection = service.getAll(SecurityUtil.authUserId());
        return MealsUtil.getWithExcess(mealCollection, MealsUtil.DEFAULT_CALORIES_PER_DAY);
    }

//
//    public List<MealTo> getTo(LocalDate startDate, LocalDate endDate) {
//        return getTo(startDate, endDate, null, null);
//    }
//
//    public List<MealTo> getTo(LocalTime startTime, LocalTime endTime) {
//        return getTo(null, null, startTime, endTime);
//    }

    public List<MealTo> getTo(LocalDate startDate, LocalDate endDate, LocalTime startTime, LocalTime endTime) {
        log.info("getTo");
        if (startDate == null) {
            startDate = LocalDate.MIN;
        }
        if (endDate == null) {
            endDate = LocalDate.MAX;
        }
        if (startTime == null) {
            startTime = LocalTime.MIN;
        }
        if (endTime == null) {
            endTime = LocalTime.MAX;
        }

        Collection<Meal> mealCollection = service.getAllBetweenDates(SecurityUtil.authUserId(), startDate, endDate);
        return MealsUtil.getFilteredWithExcess(mealCollection, SecurityUtil.authUserCaloriesPerDay(), startTime, endTime);
    }

    public Meal get(int id) {
        log.info("get {}", id);
        return service.get(id, SecurityUtil.authUserId());
    }

    public Meal create(Meal meal) {
        log.info("create {}", meal);
        checkNew(meal);
        return service.create(meal, SecurityUtil.authUserId());
    }

    public void delete(int id) {
        log.info("delete {}", id);
        service.delete(id, SecurityUtil.authUserId());
    }

    public void update(Meal meal, int id) {
        log.info("update {} with id={}", meal, id);
        assureIdConsistent(meal, id);
        service.update(meal, SecurityUtil.authUserId());
    }
}
