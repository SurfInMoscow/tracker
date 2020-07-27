package ru.vorobyev.tracker;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan(basePackages = {
        "ru.vorobyev.tracker.repository.jpa",
        "ru.vorobyev.tracker.service"
})
public class TrackerConfiguration {
}
