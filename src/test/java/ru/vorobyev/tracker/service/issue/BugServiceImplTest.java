package ru.vorobyev.tracker.service.issue;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import ru.vorobyev.tracker.domain.issue.Bug;
import ru.vorobyev.tracker.repository.inmemory.issue.BugRepositoryImpl;
import ru.vorobyev.tracker.service.IssueService;

import java.util.List;

import static org.junit.Assert.*;
import static ru.vorobyev.tracker.service.issue.IssueTestData.*;

public class BugServiceImplTest {

    private static IssueService<Bug> bugService;

    /*Sequence для ID стартует со 100*/

    @BeforeClass
    public static void setUp() {
        bugService = new BugServiceImpl(new BugRepositoryImpl());
        bugService.save(BUG1);
        bugService.save(BUG2);
    }

    @Test
    public void save() {
        bugService.save(BUG3);

        Bug tmpBug = bugService.get(103);

        assertNotNull(tmpBug);

        tmpBug.setName("New name");

        bugService.save(tmpBug);

        assertEquals(tmpBug, bugService.get(103));
    }

    @Test
    public void delete() {
        bugService.delete(101);

        assertNull(bugService.get(101));
    }

    @Test
    public void get() {
        Bug tmpBug = bugService.get(102);
        assertEquals("Second bug", tmpBug.getName());
    }

    @Test
    public void getByName() {
        Bug tmpBug = bugService.getByName("Second bug");

        assertEquals("Second bug", tmpBug.getName());
    }

    @Test
    public void getAll() {
        List<Bug> bugs = bugService.getAll();

        assertNotSame(0, bugs.size());

        bugs.forEach(Assert::assertNotNull);
    }
}