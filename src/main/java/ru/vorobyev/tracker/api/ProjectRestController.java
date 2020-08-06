package ru.vorobyev.tracker.api;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping(value = "/api/projects", produces = APPLICATION_JSON_VALUE)
public class ProjectRestController {
}
