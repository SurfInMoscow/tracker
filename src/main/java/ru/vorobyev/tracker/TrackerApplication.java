package ru.vorobyev.tracker;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;
import ru.vorobyev.tracker.config.TrackerConfig;

@SpringBootApplication
@Import(TrackerConfig.class)
public class TrackerApplication {

    public static void main(String[] args) {
        SpringApplication.run(TrackerApplication.class, args);
    }

}
