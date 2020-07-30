package ru.vorobyev.tracker;

import org.junit.runner.RunWith;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@ActiveProfiles("jdbc")
public abstract class AbstractJdbcServiceTest extends AbstractServiceTest {
}
