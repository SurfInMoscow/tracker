package ru.vorobyev.tracker;

import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import ru.vorobyev.tracker.config.TrackerJdbcConfig;
import ru.vorobyev.tracker.config.TrackerJpaConfig;
import ru.vorobyev.tracker.config.TrackerConfig;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes = {TrackerConfig.class,
        TrackerJpaConfig.class,
        TrackerJdbcConfig.class})
public abstract class AbstractServiceTest {
}
