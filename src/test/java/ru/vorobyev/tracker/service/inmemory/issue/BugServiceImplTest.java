package ru.vorobyev.tracker.service.inmemory.issue;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import ru.vorobyev.tracker.domain.issue.Bug;
import ru.vorobyev.tracker.repository.inmemory.issue.BugRepositoryImpl;
import ru.vorobyev.tracker.service.IssueService;
import ru.vorobyev.tracker.service.issue.BugServiceImpl;

import java.util.List;

import static org.junit.Assert.*;
import static ru.vorobyev.tracker.service.inmemory.issue.IssueTestData.BUG2;
import static ru.vorobyev.tracker.service.inmemory.issue.IssueTestData.BUG3;

public class BugServiceImplTest {

    private static IssueService<Bug> bugService;

    /*Sequence для ID стартует со 100*/

    @BeforeClass
    public static void setUp() {
        bugService = new BugServiceImpl(new BugRepositoryImpl());
        bugService.save(IssueTestData.BUG1);
        bugService.save(IssueTestData.BUG2);
    }

    @Test
    public void save() {
        Bug bug = new Bug(BUG3);
        bugService.save(bug);

        Bug tmpBug = bugService.save(bug);

        assertNotNull(tmpBug);

        tmpBug.setName("New name");

        bugService.save(tmpBug);

        assertEquals(tmpBug, bugService.get(tmpBug.getId()));
    }

    @Test
    public void delete() {
        bugService.delete(101);

        assertNull(bugService.get(101));
    }

    @Test
    public void get() {
        Bug bug = bugService.save(new Bug(BUG2));

        Bug tmpBug = bugService.get(bug.getId());

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