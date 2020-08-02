package ru.vorobyev.tracker.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.vorobyev.tracker.domain.user.User;
import ru.vorobyev.tracker.service.UserService;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Controller
@RequestMapping("/tracker")
public class RegistrationController {

    private final UserService userService;

    @Autowired
    public RegistrationController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    protected String login() {
        return "login";
    }

    @PostMapping
    protected String enter(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        req.setCharacterEncoding("UTF-8");
        String email = req.getParameter("email");
        String password = req.getParameter("password");
        User user;

        try {
            user = userService.getByEmail(email);
        } catch (Exception e) {
            return "login";
        }

        if (user.getPassword().equals(password)) {
            Cookie cookie = new Cookie("usr_id", String.valueOf(user.getId()));
            cookie.setMaxAge(-1);
            cookie.setPath("/");
            resp.addCookie(cookie);

            return "redirect:/projects";
        } else {
            return "login";
        }
    }

}
