package ru.vorobyev.tracker.service.inmemory.project;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import ru.vorobyev.tracker.domain.project.Project;
import ru.vorobyev.tracker.repository.inmemory.project.ProjectRepositoryImpl;
import ru.vorobyev.tracker.service.ProjectService;
import ru.vorobyev.tracker.service.project.ProjectServiceImpl;

import java.util.List;

import static org.junit.Assert.*;

public class ProjectServiceImplTest {

    private static ProjectService projectService;

    @BeforeClass
    public static void setUp() {
        projectService = new ProjectServiceImpl(new ProjectRepositoryImpl());
    }

    @Test
    public void save() {
        Project project = projectService.save(ProjectTestData.PROJECT1);

        assertNotNull(project.getId());
    }

    @Test
    public void delete() {
        Project project2 = projectService.save(ProjectTestData.PROJECT2);
        Project project3 = projectService.save(ProjectTestData.PROJECT3);

        assertTrue(projectService.delete(project3.getId()));

        assertNull(projectService.get(project3.getId()));

        ProjectTestData.PROJECT3.setId(null);
    }

    @Test
    public void get() {
        Project project = projectService.save(ProjectTestData.PROJECT3);

        assertEquals(project.getId(), projectService.get(project.getId()).getId());
    }

    @Test
    public void getByName() {
        ProjectTestData.PROJECT1.setName("Secret project");

        projectService.save(ProjectTestData.PROJECT1);

        assertEquals("Secret project", projectService.getByName("Secret project").getName());
    }

    @Test
    public void getAll() {
        projectService.save(ProjectTestData.PROJECT1);
        projectService.save(ProjectTestData.PROJECT2);
        projectService.save(ProjectTestData.PROJECT3);
        List<Project> projects = projectService.getAll();

        assertNotSame(0, projects.size());

        projects.forEach(Assert::assertNotNull);
    }
}