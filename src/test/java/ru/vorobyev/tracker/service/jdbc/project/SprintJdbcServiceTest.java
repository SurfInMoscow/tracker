package ru.vorobyev.tracker.service.jdbc.project;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import ru.vorobyev.tracker.domain.project.Sprint;
import ru.vorobyev.tracker.repository.jdbc.project.SprintJdbcRepositoryImpl;
import ru.vorobyev.tracker.service.SprintService;
import ru.vorobyev.tracker.service.project.SprintServiceImpl;

import java.util.List;

import static org.junit.Assert.*;
import static ru.vorobyev.tracker.service.project.ProjectTestData.*;

public class SprintJdbcServiceTest {

    private static SprintService sprintService;

    @BeforeClass
    public static void setUp() {
        SprintJdbcRepositoryImpl sprintRepository = new SprintJdbcRepositoryImpl();
        sprintService = new SprintServiceImpl(sprintRepository);
        sprintRepository.clear();
    }

    @Test
    public void save() {
        Sprint sprint = sprintService.save(SPRINT1);

        assertNotNull(sprint);

        assertNotNull(sprint.getId());
    }

    @Test
    public void delete() {
        Sprint sprint = sprintService.save(SPRINT2);

        assertTrue(sprintService.delete(sprint.getId()));
    }

    @Test
    public void get() {
        Sprint sprint = sprintService.save(SPRINT3);

        assertNotNull(sprint);

        assertEquals(sprint, sprintService.get(sprint.getId()));
    }

    @Test
    public void getAll() {
        Sprint sprint1= new Sprint();
        Sprint sprint2= new Sprint();
        Sprint sprint3= new Sprint();

        sprint1 = sprintService.save(sprint1);
        sprint2 = sprintService.save(sprint2);
        sprint3 = sprintService.save(sprint3);

        List<Sprint> sprints = sprintService.getAll();

        assertTrue(sprints.contains(sprint1));
        assertTrue(sprints.contains(sprint2));
        assertTrue(sprints.contains(sprint3));

        sprints.forEach(sprint -> assertNotNull(sprint.getId()));
    }
}
