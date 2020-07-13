package ru.vorobyev.tracker.service.project;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import ru.vorobyev.tracker.domain.project.Project;
import ru.vorobyev.tracker.repository.inmemory.project.ProjectRepositoryImpl;
import ru.vorobyev.tracker.service.ProjectService;

import java.util.List;

import static org.junit.Assert.*;
import static ru.vorobyev.tracker.service.project.ProjectTestData.*;

public class ProjectServiceImplTest {

    private static ProjectService projectService;

    @BeforeClass
    public static void setUp() {
        projectService = new ProjectServiceImpl(new ProjectRepositoryImpl());
    }

    @Test
    public void save() {
        Project project = projectService.save(PROJECT1);

        assertNotNull(project.getId());
    }

    @Test
    public void delete() {
        Project project2 = projectService.save(PROJECT2);
        Project project3 = projectService.save(PROJECT3);

        assertTrue(projectService.delete(project3.getId()));

        assertNull(projectService.get(project3.getId()));

        PROJECT3.setId(null);
    }

    @Test
    public void get() {
        Project project = projectService.save(PROJECT3);

        assertEquals(project.getId(), projectService.get(project.getId()).getId());
    }

    @Test
    public void getByName() {
        PROJECT1.setName("Secret project");

        projectService.save(PROJECT1);

        assertEquals("Secret project", projectService.getByName("Secret project").getName());
    }

    @Test
    public void getAll() {
        projectService.save(PROJECT1);
        projectService.save(PROJECT2);
        projectService.save(PROJECT3);
        List<Project> projects = projectService.getAll();

        assertNotSame(0, projects.size());

        projects.forEach(Assert::assertNotNull);
    }
}