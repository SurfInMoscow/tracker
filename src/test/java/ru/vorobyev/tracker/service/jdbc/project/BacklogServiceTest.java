package ru.vorobyev.tracker.service.jdbc.project;

import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import ru.vorobyev.tracker.domain.project.Backlog;
import ru.vorobyev.tracker.repository.jdbc.project.BacklogJdbcRepositoryImpl;
import ru.vorobyev.tracker.service.BacklogService;
import ru.vorobyev.tracker.AbstractJdbcServiceTest;

import java.util.List;

import static org.junit.Assert.*;
import static ru.vorobyev.tracker.service.jdbc.project.ProjectJdbcTestData.*;


public class BacklogServiceTest extends AbstractJdbcServiceTest {

    @Autowired
    private BacklogService backlogService;

    @BeforeClass
    public static void setUp() {
        BacklogJdbcRepositoryImpl backlogRepository = new BacklogJdbcRepositoryImpl();
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

    @Test
    public void get() {
        Backlog backlog = backlogService.save(BACKLOG3);

        assertNotNull(backlog);

        assertEquals(backlog, backlogService.get(backlog.getId()));
    }

    @Test
    public void getAll() {
        Backlog backlog1 = new Backlog();
        Backlog backlog2 = new Backlog();
        Backlog backlog3 = new Backlog();

       backlog1 = backlogService.save(backlog1);
       backlog2 = backlogService.save(backlog2);
       backlog3 = backlogService.save(backlog3);

        List<Backlog> backlogs = backlogService.getAll();

        assertTrue(backlogs.contains(backlog1));
        assertTrue(backlogs.contains(backlog2));
        assertTrue(backlogs.contains(backlog3));

        backlogs.forEach(backlog -> assertNotNull(backlog.getId()));
    }
}
