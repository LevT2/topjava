package ru.javawebinar.topjava.web.meal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.service.MealService;
import ru.javawebinar.topjava.to.MealTo;

import java.util.Collection;
import java.util.List;

@Controller
public class MealRestController extends AbstractMealController{

    @Autowired
    public MealRestController (MealService service){
        this.service = service;
    }

    @Override
    public Collection<Meal> getAll(int userId) {
        return super.getAll(userId);
    }

    @Override
    public Meal get(int id, int userId) {
        return super.get(id, userId);
    }

    @Override
    public Meal create(Meal meal, int userId) {
        return super.create(meal, userId);
    }

    @Override
    public void delete(int id, int userId) {
        super.delete(id, userId);
    }

    @Override
    public void update(Meal meal, int id, int userId) {
        super.update(meal, id, userId);
    }
}
