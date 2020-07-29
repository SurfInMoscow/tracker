package ru.vorobyev.tracker;

import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles("jdbc")
public abstract class AbstractJdbcServiceTest extends AbstractServiceTest {
}
