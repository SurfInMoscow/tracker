package ru.vorobyev.tracker.service.jdbc.issue;

import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import ru.vorobyev.tracker.AbstractJdbcServiceTest;
import ru.vorobyev.tracker.domain.issue.Bug;
import ru.vorobyev.tracker.repository.jdbc.issue.BugJdbcRepositoryImpl;
import ru.vorobyev.tracker.service.IssueService;

import java.util.List;

import static org.junit.Assert.*;
import static ru.vorobyev.tracker.service.jdbc.issue.IssueJdbcTestData.*;

public class BugJdbcServiceTest extends AbstractJdbcServiceTest {

    @Autowired
    public IssueService<Bug> issueBugService;

    @BeforeClass
    public static void setUp() {
        BugJdbcRepositoryImpl bugJdbcRepository = new BugJdbcRepositoryImpl();
        bugJdbcRepository.clear();
    }

    @Test
    public void save() {
        Bug bug = issueBugService.save(BUG1);

        assertNotNull(bug);

        bug.setName("New bug");

        int id = bug.getId();

        bug = issueBugService.save(bug);

        assertEquals(id, (int) bug.getId());

        assertEquals("New bug", bug.getName());
    }

    @Test
    public void delete() {
        Bug bug = issueBugService.save(BUG2);

        assertNotNull(bug);

        assertTrue(issueBugService.delete(bug.getId()));
    }

    @Test
    public void get() {
        Bug bug = issueBugService.save(BUG3);

        assertNotNull(bug);

        bug = issueBugService.get(bug.getId());

        assertEquals(bug.getId(), issueBugService.get(bug.getId()).getId());
    }

    @Test
    public void getByName() {
        Bug bug = issueBugService.save(BUG3);

        assertNotNull(bug);

        bug = issueBugService.getByName(bug.getName());

        assertEquals(bug.getName(), issueBugService.getByName(bug.getName()).getName());
    }

    @Test
    public void getAll() {
        Bug bug1 = new Bug(BUG1);
        Bug bug2 = new Bug(BUG2);
        Bug bug3 = new Bug(BUG3);

        bug1 = issueBugService.save(bug1);
        bug2 = issueBugService.save(bug2);
        bug3 = issueBugService.save(bug3);

        List<Bug> bugs = issueBugService.getAll();

        assertNotEquals(0, bugs.size());

        bugs.forEach(bug -> assertNotNull(bug.getId()));
    }
}
