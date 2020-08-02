package ru.vorobyev.tracker.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@ComponentScan(basePackages = {"ru.vorobyev.tracker.service"})
@Import({TrackerJpaConfig.class, TrackerJdbcConfig.class, TrackerWebConfig.class})
public class TrackerConfig {
}