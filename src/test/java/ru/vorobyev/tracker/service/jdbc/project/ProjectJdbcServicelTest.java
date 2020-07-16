package ru.vorobyev.tracker.service.jdbc.project;

import org.junit.BeforeClass;
import org.junit.Test;
import ru.vorobyev.tracker.repository.jdbc.project.ProjectJdbcRepositoryImpl;
import ru.vorobyev.tracker.service.ProjectService;
import ru.vorobyev.tracker.service.project.ProjectServiceImpl;

import static org.junit.Assert.*;

public class ProjectJdbcServicelTest {

    private static ProjectService projectService;

    @BeforeClass
    public static void setUp() {
        projectService = new ProjectServiceImpl(new ProjectJdbcRepositoryImpl());
    }

    @Test
    public void save() {
    }

    @Test
    public void delete() {
    }

    @Test
    public void get() {
    }

    @Test
    public void getByName() {
    }

    @Test
    public void getAll() {
    }
}