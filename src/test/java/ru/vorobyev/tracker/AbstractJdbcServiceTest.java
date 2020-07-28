package ru.vorobyev.tracker;

import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import ru.vorobyev.tracker.config.TrackerSpringConfig;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes = TrackerSpringConfig.class)
public abstract class AbstractJdbcServiceTest {
}
