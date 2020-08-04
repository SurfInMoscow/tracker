package ru.vorobyev.tracker.service.jpa.project;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import ru.vorobyev.tracker.AbstractJpaServiceTest;
import ru.vorobyev.tracker.domain.project.Sprint;
import ru.vorobyev.tracker.service.SprintService;

import java.util.List;

import static org.junit.Assert.*;
import static ru.vorobyev.tracker.service.jpa.project.ProjectJpaTestData.SPRINT1;
import static ru.vorobyev.tracker.service.jpa.project.ProjectJpaTestData.SPRINT2;

public class SprintServiceTest extends AbstractJpaServiceTest {

    @Autowired
    private SprintService sprintService;

    @Test
    public void save() {
        Sprint sprint = sprintService.save(SPRINT1);

        assertNotNull(sprint.getId());
    }

    @Test
    public void delete() {
        Sprint sprint = sprintService.save(SPRINT2);

        assertTrue(sprintService.delete(sprint.getId()));

        assertNull(sprintService.get(sprint.getId()));
    }

    @Test
    public void get() {
        Sprint sprint = sprintService.get(100_005);

        assertNotNull(sprint.getId());
    }

    @Test
    public void getAll() {
        List<Sprint> sprints = sprintService.getAll();

        assertTrue(sprints.size() > 1);

        sprints.forEach(sprint -> assertNotNull(sprint.getId()));
    }
}