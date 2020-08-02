package ru.vorobyev.tracker.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.view.InternalResourceViewResolver;

@Configuration
@ComponentScan(basePackages = {
        "ru.vorobyev.tracker.web"
})
public class TrackerWebConfig implements WebMvcConfigurer {
    @Bean
    public InternalResourceViewResolver jspViewResolver() {
        InternalResourceViewResolver bean = new InternalResourceViewResolver();
        bean.setPrefix("/WEB-INF/templates/");
        bean.setSuffix(".jsp");
        return bean;
    }
}
