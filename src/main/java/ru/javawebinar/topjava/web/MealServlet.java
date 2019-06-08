package ru.javawebinar.topjava.web;

import org.slf4j.Logger;
import ru.javawebinar.topjava.model.MealTo;
import ru.javawebinar.topjava.util.MealsUtil;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

import static org.slf4j.LoggerFactory.getLogger;

public class MealServlet extends HttpServlet {
    private static String MEALS = "meals.jsp";
    private List<MealTo> list;

    private static final Logger log = getLogger(MealServlet.class);

    public MealServlet() {
        super();
        list = MealsUtil.hardcodedMealTo();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setAttribute("mealsWithExceed", list);

        log.debug("redirect to meals");
        request.getRequestDispatcher(MEALS).forward(request, response);
    }
}
