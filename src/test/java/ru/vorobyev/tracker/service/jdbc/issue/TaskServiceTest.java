package ru.vorobyev.tracker.service.jdbc.issue;

import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import ru.vorobyev.tracker.domain.issue.Task;
import ru.vorobyev.tracker.repository.jdbc.issue.TaskJdbcRepositoryImpl;
import ru.vorobyev.tracker.service.IssueService;
import ru.vorobyev.tracker.AbstractJdbcServiceTest;

import java.util.List;

import static org.junit.Assert.*;
import static ru.vorobyev.tracker.service.jdbc.issue.IssueJdbcTestData.*;

public class TaskServiceTest extends AbstractJdbcServiceTest {

    @Autowired
    private IssueService<Task> issueTaskService;

    @BeforeClass
    public static void setUp() {
        TaskJdbcRepositoryImpl taskJdbcRepository = new TaskJdbcRepositoryImpl();
        taskJdbcRepository.clear();
    }

    @Test
    public void save() {
        Task task = issueTaskService.save(TASK1);

        assertNotNull(task);

        task.setName("New task");

        int id = task.getId();

        task = issueTaskService.save(task);

        assertEquals(id, (int) task.getId());

        assertEquals("New task", task.getName());
    }

    @Test
    public void delete() {
        Task task = issueTaskService.save(TASK2);

        assertNotNull(task);

        assertTrue(issueTaskService.delete(task.getId()));
    }

    @Test
    public void get() {
        Task task = issueTaskService.save(TASK3);

        assertNotNull(task);

        task = issueTaskService.get(task.getId());

        assertEquals(task.getId(), issueTaskService.get(task.getId()).getId());
    }

    @Test
    public void getByName() {
        Task task = issueTaskService.save(TASK3);

        assertNotNull(task);

        task = issueTaskService.getByName(task.getName());

        assertEquals(task.getName(), issueTaskService.getByName(task.getName()).getName());
    }

    @Test
    public void getAll() {
        Task task1 = new Task(TASK1);
        Task task2 = new Task(TASK2);
        Task task3 = new Task(TASK3);

        task1 = issueTaskService.save(task1);
        task2 = issueTaskService.save(task2);
        task3 = issueTaskService.save(task3);

        List<Task> tasks = issueTaskService.getAll();

        assertNotEquals(0, tasks.size());

        tasks.forEach(task -> assertNotNull(task.getId()));
    }
    
}
