package ru.vorobyev.tracker.service.jdbc.project;

import org.junit.BeforeClass;
import org.junit.Test;
import ru.vorobyev.tracker.domain.project.Project;
import ru.vorobyev.tracker.repository.jdbc.project.ProjectJdbcRepositoryImpl;
import ru.vorobyev.tracker.service.ProjectService;
import ru.vorobyev.tracker.service.project.ProjectServiceImpl;

import java.util.List;

import static org.junit.Assert.*;
import static ru.vorobyev.tracker.service.jdbc.project.ProjectJdbcTestData.*;


public class ProjectJdbcServiceTest {

    private static ProjectService projectService;

    @BeforeClass
    public static void setUp() {
        ProjectJdbcRepositoryImpl projectRepository = new ProjectJdbcRepositoryImpl();
        projectService = new ProjectServiceImpl(projectRepository);
        projectRepository.clear();
    }

    @Test
    public void save() {
        Project project = projectService.save(PROJECT1);

        assertNotNull(project);

        assertNotNull(project.getId());

        project.setName("Updated project");

        projectService.save(project);

        assertEquals("Updated project", project.getName());
    }

    @Test
    public void delete() {
        Project project = projectService.save(PROJECT2);

        assertNotNull(project.getId());

        assertTrue(projectService.delete(project.getId()));
    }

    @Test
    public void get() {
        Project project = projectService.save(PROJECT3);

        assertNotNull(project);

        project = projectService.get(project.getId());

        assertEquals(project.getId(), projectService.get(project.getId()).getId());
    }

    @Test
    public void getByName() {
        Project project = projectService.save(PROJECT3);

        assertNotNull(project);

        project.setName("Test-Project");

        projectService.save(project);

        Project project1 = projectService.getByName(project.getName());

        assertEquals(project.getName(), project1.getName());
    }

    @Test
    public void getAll() {
        Project project1 = new Project("Test1", "unit-test project", "java department", "Manager", "Admin");
        Project project2 = new Project("Test1", "unit-test project", "java department", "Manager", "Admin");
        Project project3 = new Project("Test1", "unit-test project", "java department", "Manager", "Admin");

        projectService.save(project1);
        projectService.save(project2);
        projectService.save(project3);

        List<Project> projects = projectService.getAll();

        assertNotEquals(0, projects.size());

        projects.forEach(project -> assertNotNull(project.getId()));
    }
}