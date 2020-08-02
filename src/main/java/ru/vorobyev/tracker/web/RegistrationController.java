package ru.vorobyev.tracker.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.vorobyev.tracker.service.UserService;

@Controller
@RequestMapping("/tracker")
public class RegistrationController {

    private final UserService userService;
    private final AuthenticationManager authenticationManagerBean;

    @Autowired
    public RegistrationController(UserService userService, AuthenticationManager authenticationManagerBean) {
        this.userService = userService;
        this.authenticationManagerBean = authenticationManagerBean;
    }

    @GetMapping
    protected String login() {
        return "login";
    }
}
