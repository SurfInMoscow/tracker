package ru.vorobyev.tracker.service.jpa.project;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import ru.vorobyev.tracker.AbstractJpaServiceTest;
import ru.vorobyev.tracker.domain.project.Backlog;
import ru.vorobyev.tracker.service.BacklogService;

import java.util.List;

import static org.junit.Assert.*;
import static ru.vorobyev.tracker.service.jpa.project.ProjectJpaTestData.BACKLOG1;
import static ru.vorobyev.tracker.service.jpa.project.ProjectJpaTestData.BACKLOG2;

public class BacklogServiceTest extends AbstractJpaServiceTest {

    @Autowired
    private BacklogService backlogService;

    @Test
    public void save() {
        Backlog backlog = backlogService.save(BACKLOG1);

        assertNotNull(backlog.getId());
    }

    @Test
    public void delete() {
        Backlog backlog = backlogService.save(BACKLOG2);

        assertTrue(backlogService.delete(backlog.getId()));

        assertNull(backlogService.get(backlog.getId()));
    }

    @Test
    public void get() {
        Backlog backlog = backlogService.get(100_003);

        assertNotNull(backlog.getId());
    }

    @Test
    public void getAll() {
        List<Backlog> backlogs = backlogService.getAll();

        assertTrue(backlogs.size() > 1);

        backlogs.forEach(backlog -> assertNotNull(backlog.getId()));
    }
}