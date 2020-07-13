package ru.vorobyev.tracker.service.project;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import ru.vorobyev.tracker.domain.project.Backlog;
import ru.vorobyev.tracker.repository.inmemory.project.BacklogRepositoryImpl;
import ru.vorobyev.tracker.service.BacklogService;

import java.util.HashSet;
import java.util.List;

import static org.junit.Assert.*;
import static ru.vorobyev.tracker.service.project.ProjectTestData.*;

public class BacklogServiceImplTest {

    private static BacklogService backlogService;

    @BeforeClass
    public static void setUp() {
        backlogService = new BacklogServiceImpl(new BacklogRepositoryImpl());
    }

    @Test
    public void save() {
        Backlog backlog = new Backlog(new HashSet<>(), new HashSet<>(), new HashSet<>(), new HashSet<>());

        backlog = backlogService.save(backlog);

        assertNotNull(backlog.getId());
    }

    @Test
    public void delete() {
        Backlog backlog1 = backlogService.save(BACKLOG1);
        Backlog backlog2 = backlogService.save(BACKLOG2);

        assertTrue(backlogService.delete(backlog1.getId()));

        assertNull(backlogService.get(backlog1.getId()));
    }

    @Test
    public void get() {
        Backlog backlog = backlogService.save(BACKLOG3);

        assertEquals(backlog.getId(), backlogService.get(backlog.getId()).getId());
    }

    @Test
    public void getAll() {
        BACKLOG1.setId(null);
        BACKLOG2.setId(null);
        backlogService.save(BACKLOG1);
        backlogService.save(BACKLOG2);

        List<Backlog> backlogs = backlogService.getAll();

        assertNotSame(0, backlogs.size());

        backlogs.forEach(Assert::assertNotNull);
    }
}