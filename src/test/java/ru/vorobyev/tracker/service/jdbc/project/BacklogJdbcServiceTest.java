package ru.vorobyev.tracker.service.jdbc.project;

import org.junit.BeforeClass;
import org.junit.Test;
import ru.vorobyev.tracker.domain.project.Backlog;
import ru.vorobyev.tracker.repository.jdbc.project.BacklogJdbcRepositoryImpl;
import ru.vorobyev.tracker.service.BacklogService;
import ru.vorobyev.tracker.service.project.BacklogServiceImpl;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static ru.vorobyev.tracker.service.project.ProjectTestData.BACKLOG1;
import static ru.vorobyev.tracker.service.project.ProjectTestData.BACKLOG2;

public class BacklogJdbcServiceTest {

    private static BacklogService backlogService;

    @BeforeClass
    public static void setUp() {
        BacklogJdbcRepositoryImpl backlogRepository = new BacklogJdbcRepositoryImpl();
        backlogService = new BacklogServiceImpl(backlogRepository);
        backlogRepository.clear();
    }

    @Test
    public void save() {
        Backlog backlog = backlogService.save(BACKLOG1);

        assertNotNull(backlog);

        assertNotNull(backlog.getId());
    }

    @Test
    public void delete() {
        Backlog backlog = backlogService.save(BACKLOG2);

        assertTrue(backlogService.delete(backlog.getId()));
    }
}
