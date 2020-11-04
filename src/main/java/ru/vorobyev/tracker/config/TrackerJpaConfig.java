package ru.vorobyev.tracker.config;

import com.zaxxer.hikari.HikariDataSource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.dao.annotation.PersistenceExceptionTranslationPostProcessor;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;
import java.util.Properties;

@Profile("jpa")
@Configuration
@EnableTransactionManagement
@ComponentScan(basePackages = {
        "ru.vorobyev.tracker.repository.jpa"
})
public class TrackerJpaConfig {

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

    @Value("${spring.jpa.properties.hibernate.dialect}")
    private String hibernateDialect;

    @Value("${spring.jpa.properties.hibernate.show_sql}")
    private String showSql;

    @Value("${spring.jpa.properties.hibernate.format_sql}")
    private String formatSql;

    @Value("${spring.jpa.properties.hibernate.use_sql_comments}")
    private String useSqlComments;

    @Value("${spring.jpa.properties.hibernate.default_batch_fetch_size}")
    private String defaultBatchFetchSize;

    @Value("${spring.jpa.properties.hibernate.enable_lazy_load_no_trans}")
    private String enableLazyLoadNoTrans;

    @Value("${spring.jpa.properties.hibernate.jdbc.batch_size}")
    private String jdbcBatchSize;

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

    @Bean
    public LocalContainerEntityManagerFactoryBean entityManagerFactory() {
        LocalContainerEntityManagerFactoryBean em = new LocalContainerEntityManagerFactoryBean();
        em.setDataSource(dataSource());
        JpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
        em.setJpaVendorAdapter(vendorAdapter);
        em.setJpaProperties(additionalProperties());
        em.setPackagesToScan("ru.vorobyev.tracker.domain");

        return em;
    }

    @Bean
    public PlatformTransactionManager transactionManager(EntityManagerFactory factory) {
        JpaTransactionManager transactionManager = new JpaTransactionManager();
        transactionManager.setEntityManagerFactory(factory);

        return transactionManager;
    }

    @Bean
    public PersistenceExceptionTranslationPostProcessor exceptionTranslation(){
        return new PersistenceExceptionTranslationPostProcessor();
    }

    public Properties additionalProperties() {
        Properties properties = new Properties();
        properties.setProperty("hibernate.show_sql", showSql);
        properties.setProperty("hibernate.dialect", hibernateDialect);
        properties.setProperty("hibernate.format_sql", formatSql);
        properties.setProperty("hibernate.use_sql_comments", useSqlComments);
        properties.setProperty("hibernate.default_batch_fetch_size", defaultBatchFetchSize);
        properties.setProperty("hibernate.enable_lazy_load_no_trans", enableLazyLoadNoTrans);
        properties.setProperty("hibernate.jdbc.batch_size", jdbcBatchSize);

        return properties;
    }
}
