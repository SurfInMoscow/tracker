package ru.vorobyev.tracker.config;

import com.zaxxer.hikari.HikariDataSource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import javax.sql.DataSource;

@Profile("jdbc")
@Configuration
@ComponentScan(basePackages = {
        "ru.vorobyev.tracker.repository.jdbc"
})
public class TrackerJdbcConfig {
    @Value("${spring.datasource.hikari.jdbc-url}")
    private String url;

    @Value("${spring.datasource.hikari.username}")
    private String username;

    @Value("${spring.datasource.hikari.password}")
    private String password;

    @Value("${spring.datasource.hikari.driver-class-name}")
    private String driverClassName;

    @Value("${spring.datasource.hikari.connection-timeout}")
    private long connectionTimeout;

    @Value("${spring.datasource.hikari.maximum-pool-size}")
    private int maximumPoolSize;

    @Bean
    public DataSource dataSource() {
        HikariDataSource dataSource = new HikariDataSource();
        dataSource.setDriverClassName(driverClassName);
        dataSource.setJdbcUrl(url);
        dataSource.setUsername(username);
        dataSource.setPassword(password);
        dataSource.setConnectionTimeout(connectionTimeout);
        dataSource.setMaximumPoolSize(maximumPoolSize);

        return dataSource;
    }
}
