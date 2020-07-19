package ru.vorobyev.tracker.service.inmemory.issue;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import ru.vorobyev.tracker.domain.issue.Epic;
import ru.vorobyev.tracker.repository.inmemory.issue.EpicRepositoryImpl;
import ru.vorobyev.tracker.service.IssueService;
import ru.vorobyev.tracker.service.issue.EpicServiceImpl;

import java.util.List;

import static org.junit.Assert.*;

public class EpicServiceImplTest {

    private static IssueService<Epic> epicService;

    /*Sequence для ID стартует со 100*/

    @BeforeClass
    public static void setUp() {
        epicService = new EpicServiceImpl(new EpicRepositoryImpl());
        epicService.save(IssueTestData.EPIC1);
        epicService.save(IssueTestData.EPIC2);
    }

    @Test
    public void save() {
        epicService.save(IssueTestData.EPIC3);

        Epic tmpEpic = epicService.get(103);

        assertNotNull(tmpEpic);

        tmpEpic.setName("New Name");

        epicService.save(tmpEpic);

        assertEquals(tmpEpic, epicService.get(103));
    }

    @Test
    public void delete() {
        epicService.delete(101);

        assertNull(epicService.get(101));
    }

    @Test
    public void get() {
        assertNotNull(epicService.get(102));
    }

    @Test
    public void getByName() {
        Epic tmpEpic = epicService.getByName("Second epic");

        assertEquals("Second epic", tmpEpic.getName());
    }

    @Test
    public void getAll() {
        List<Epic> epics = epicService.getAll();

        assertNotSame(0, epics.size());

        epics.forEach(Assert::assertNotNull);
    }
}