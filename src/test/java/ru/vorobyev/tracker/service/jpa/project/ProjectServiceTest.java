package ru.vorobyev.tracker.service.jpa.project;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import ru.vorobyev.tracker.AbstractJpaServiceTest;
import ru.vorobyev.tracker.domain.project.Project;
import ru.vorobyev.tracker.service.ProjectService;

import java.util.List;

import static org.junit.Assert.*;
import static ru.vorobyev.tracker.service.jpa.project.ProjectJpaTestData.PROJECT1;
import static ru.vorobyev.tracker.service.jpa.project.ProjectJpaTestData.PROJECT2;

public class ProjectServiceTest extends AbstractJpaServiceTest {

    @Autowired
    private ProjectService projectService;

    @Test
    public void save() {
        Project project = projectService.save(PROJECT1);

        assertNotNull(project.getId());

        project.setName("New Project Name");

        project = projectService.save(project);

        assertEquals("New Project Name", project.getName());

    }

    //Backlog и Sprint из проета нужн оудалять отдельно, т.к. между сущностями нет двусторонней связи.
    @Test
    public void delete() {
        Project project = projectService.save(PROJECT2);

        assertTrue(projectService.delete(project.getId()));

        assertNull(projectService.get(project.getId()));
    }

    @Test
    public void get() {
        Project project = projectService.get(100_007);

        assertNotNull(project.getId());
    }

    @Test
    public void getByName() {
        Project project = projectService.getByName("Google ML");

        assertEquals("Google ML", project.getName());
    }

    @Test
    public void getAll() {
        List<Project> projects = projectService.getAll();

        assertTrue(projects.size() > 1);

        projects.forEach(project -> assertNotNull(project.getId()));
    }
}