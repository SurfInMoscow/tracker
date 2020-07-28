package ru.vorobyev.tracker.web;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import ru.vorobyev.tracker.config.TrackerSpringConfig;
import ru.vorobyev.tracker.domain.user.User;
import ru.vorobyev.tracker.service.UserService;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class UserRegistrationServlet extends HttpServlet {

    private UserService userService;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        AnnotationConfigApplicationContext apCtx = new AnnotationConfigApplicationContext(TrackerSpringConfig.class);
        userService = apCtx.getBean(UserService.class);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.getRequestDispatcher("/WEB-INF/templates/login.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        String email = req.getParameter("email");
        String password = req.getParameter("password");
        User user;
        try {
            user = userService.getByEmail(email);
        } catch (Exception e) {
            req.getRequestDispatcher("/WEB-INF/templates/login.jsp").forward(req, resp);
            return;
        }

        if (user.getPassword().equals(password)) {
            Cookie cookie = new Cookie("usr_id", String.valueOf(user.getId()));
            cookie.setMaxAge(1000);
            resp.addCookie(cookie);
            resp.sendRedirect("projects");
        } else {
            req.getRequestDispatcher("/WEB-INF/templates/login.jsp").forward(req, resp);
        }
    }
}
