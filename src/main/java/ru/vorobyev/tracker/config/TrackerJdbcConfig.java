package ru.vorobyev.tracker.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Profile("jdbc")
@Configuration
@ComponentScan(basePackages = {
        "ru.vorobyev.tracker.repository.jdbc"
})
public class TrackerJdbcConfig {
}
