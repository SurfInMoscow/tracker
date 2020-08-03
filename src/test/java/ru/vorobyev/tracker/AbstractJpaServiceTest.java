package ru.vorobyev.tracker;

import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlConfig;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@ActiveProfiles({"jpa", "dev", "prod"})
@ExtendWith(SpringExtension.class)
@Sql(scripts = {"classpath:data.sql"}, config = @SqlConfig(encoding = "UTF-8"))
public abstract class AbstractJpaServiceTest extends AbstractServiceTest {
}
