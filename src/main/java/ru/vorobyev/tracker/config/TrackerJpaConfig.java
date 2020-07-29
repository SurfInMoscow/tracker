package ru.vorobyev.tracker.config;

import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

@Profile("jpa")
@Configuration
@ComponentScan(basePackages = {
        "ru.vorobyev.tracker.repository.jpa"
})
public class TrackerJpaConfig {

    public void test() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        DataSourceBuilder builder = DataSourceBuilder.create();
    }
}
