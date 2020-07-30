package ru.vorobyev.tracker;

import org.springframework.boot.test.context.SpringBootTest;
import ru.vorobyev.tracker.config.TrackerConfig;
import ru.vorobyev.tracker.config.TrackerJdbcConfig;
import ru.vorobyev.tracker.config.TrackerJpaConfig;

@SpringBootTest(classes = {TrackerConfig.class,
        TrackerJpaConfig.class,
        TrackerJdbcConfig.class})
public abstract class AbstractServiceTest {
}
