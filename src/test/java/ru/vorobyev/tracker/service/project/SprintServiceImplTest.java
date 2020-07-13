package ru.vorobyev.tracker.service.project;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import ru.vorobyev.tracker.domain.project.Sprint;
import ru.vorobyev.tracker.repository.inmemory.project.SprintRepositoryImpl;
import ru.vorobyev.tracker.service.SprintService;

import java.util.List;

import static org.junit.Assert.*;
import static ru.vorobyev.tracker.service.project.ProjectTestData.*;

public class SprintServiceImplTest {

    private static SprintService sprintService;

    @BeforeClass
    public static void setUp() {
        sprintService = new SprintServiceImpl(new SprintRepositoryImpl());
    }

    @Test
    public void save() {
        Sprint sprint = sprintService.save(SPRINT1);

        assertNotNull(sprint.getId());
    }

    @Test
    public void delete() {
        sprintService.save(SPRINT2);
        sprintService.save(SPRINT3);

        assertTrue(sprintService.delete(SPRINT3.getId()));

        assertNull(sprintService.get(SPRINT3.getId()));

        SPRINT3.setId(null);
    }

    @Test
    public void get() {
        Sprint sprint = sprintService.save(SPRINT1);

        assertEquals(sprint.getId(), sprintService.get(sprint.getId()).getId());
    }

    @Test
    public void getAll() {
        sprintService.save(SPRINT1);
        sprintService.save(SPRINT2);
        sprintService.save(SPRINT3);

        List<Sprint> sprints = sprintService.getAll();

        assertNotSame(0, sprints.size());

        sprints.forEach(Assert::assertNotNull);
    }
}