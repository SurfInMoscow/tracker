package ru.vorobyev.tracker.service.inmemory.issue;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import ru.vorobyev.tracker.domain.issue.Task;
import ru.vorobyev.tracker.repository.inmemory.issue.TaskRepositoryImpl;
import ru.vorobyev.tracker.service.IssueService;
import ru.vorobyev.tracker.service.issue.TaskServiceImpl;

import java.util.List;

import static org.junit.Assert.*;

public class TaskServiceImplTest {

    private static IssueService<Task> taskService;

    /*Sequence для ID стартует со 100*/

    @BeforeClass
    public static void setUp() {
        taskService = new TaskServiceImpl(new TaskRepositoryImpl());
        taskService.save(IssueTestData.TASK1);
        taskService.save(IssueTestData.TASK2);
    }

    @Test
    public void save() {

        Task tmpTask = taskService.save(IssueTestData.TASK3);

        assertNotNull(tmpTask);

        tmpTask.setName("New Name");

        taskService.save(tmpTask);

        assertEquals(tmpTask, taskService.get(tmpTask.getId()));
    }

    @Test
    public void delete() {
        Task task = taskService.save(IssueTestData.TASK1);
        assertTrue(taskService.delete(task.getId()));
    }

    @Test
    public void get() {
        Task task = taskService.save(IssueTestData.TASK2);

        assertNotNull(taskService.get(task.getId()));
    }

    @Test
    public void getByName() {
        Task task = taskService.save(IssueTestData.TASK2);

        Task tmpTask = taskService.getByName(task.getName());

        assertEquals("Second task", tmpTask.getName());
    }

    @Test
    public void getAll() {
        List<Task> tasks = taskService.getAll();

        assertNotSame(0, tasks.size());

        tasks.forEach(Assert::assertNotNull);
    }
}