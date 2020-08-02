package ru.vorobyev.tracker.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/tracker")
public class RegistrationController {
    @GetMapping
    protected String login() {
        return "login";
    }
}
