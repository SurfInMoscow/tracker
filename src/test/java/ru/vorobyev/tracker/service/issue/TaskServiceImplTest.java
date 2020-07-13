package ru.vorobyev.tracker.service.issue;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import ru.vorobyev.tracker.domain.issue.Task;
import ru.vorobyev.tracker.repository.inmemory.issue.TaskRepositoryImpl;
import ru.vorobyev.tracker.service.IssueService;

import java.util.List;

import static org.junit.Assert.*;
import static ru.vorobyev.tracker.service.issue.IssueTestData.*;

public class TaskServiceImplTest {

    private static IssueService<Task> taskService;

    /*Sequence для ID стартует со 100*/

    @BeforeClass
    public static void setUp() {
        taskService = new TaskServiceImpl(new TaskRepositoryImpl());
        taskService.save(TASK1);
        taskService.save(TASK2);
    }

    @Test
    public void save() {
        taskService.save(TASK3);

        Task tmpTask = taskService.get(103);

        assertNotNull(tmpTask);

        tmpTask.setName("New Name");

        taskService.save(tmpTask);

        assertEquals(tmpTask, taskService.get(103));
    }

    @Test
    public void delete() {
        assertTrue(taskService.delete(101));
    }

    @Test
    public void get() {
        assertNotNull(taskService.get(102));
    }

    @Test
    public void getByName() {
        Task tmpTask = taskService.getByName("Second task");

        assertEquals("Second task", tmpTask.getName());
    }

    @Test
    public void getAll() {
        List<Task> tasks = taskService.getAll();

        assertNotSame(0, tasks.size());

        tasks.forEach(Assert::assertNotNull);
    }
}