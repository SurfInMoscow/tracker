package ru.vorobyev.tracker.service.jpa.issue;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import ru.vorobyev.tracker.AbstractJpaServiceTest;
import ru.vorobyev.tracker.domain.issue.Bug;
import ru.vorobyev.tracker.service.IssueService;

import java.util.List;

import static org.junit.Assert.*;
import static ru.vorobyev.tracker.service.jpa.issue.IssueJpaTestData.BUG1;

public class BugServiceTest extends AbstractJpaServiceTest {

    @Autowired
    private IssueService<Bug> issueService;

    @Test
    public void save() {
        Bug bug = issueService.save(BUG1);

        assertNotNull(bug.getId());
    }

    @Test
    public void delete() {
        assertTrue(issueService.delete(100_009));

        assertNull(issueService.get(100_009));
    }

    @Test
    public void get() {
        Bug bug = issueService.get(100_009);

        assertNotNull(bug.getId());
    }

    @Test
    public void getByName() {
        Bug bug = issueService.getByName("bug1");

        assertNotNull(bug.getId());
    }

    @Test
    public void getAll() {
        List<Bug> bugs = issueService.getAll();

        assertTrue(bugs.size() > 1);

        bugs.forEach(bug -> assertNotNull(bug.getId()));
    }
}