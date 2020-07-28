package ru.vorobyev.tracker.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan(basePackages = {
        "ru.vorobyev.tracker.repository.jdbc",
        "ru.vorobyev.tracker.service"
})
public class TrackerSpringConfig {
}
