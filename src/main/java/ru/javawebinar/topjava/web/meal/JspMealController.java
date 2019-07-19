package ru.javawebinar.topjava.web.meal;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.javawebinar.topjava.service.MealService;
import ru.javawebinar.topjava.to.MealTo;
import ru.javawebinar.topjava.util.MealsUtil;
import ru.javawebinar.topjava.web.SecurityUtil;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

import static org.slf4j.LoggerFactory.getLogger;

@Controller
@RequestMapping("/meals")
public class JspMealController {
    private static final Logger log = getLogger(JspMealController.class);


    @Autowired
    private MealService service;

    @GetMapping() //"/meals")
    public String meals(Model model) {
        log.info("MealTo: gatAll");
        List<MealTo> meals = MealsUtil.getWithExcess(service.getAll(SecurityUtil.authUserId()), SecurityUtil.authUserCaloriesPerDay());
        model.addAttribute("meals", meals);
        return "meals";
    }

    @PostMapping() //"/meals")
    public String setMeal(HttpServletRequest request) {
        return "redirect:users";
    }


}
