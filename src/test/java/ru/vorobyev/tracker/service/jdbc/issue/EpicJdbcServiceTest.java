package ru.vorobyev.tracker.service.jdbc.issue;

import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import ru.vorobyev.tracker.AbstractJdbcServiceTest;
import ru.vorobyev.tracker.domain.issue.Epic;
import ru.vorobyev.tracker.repository.jdbc.issue.EpicJdbcRepositoryImpl;
import ru.vorobyev.tracker.service.IssueService;

import java.util.List;

import static org.junit.Assert.*;
import static ru.vorobyev.tracker.service.jdbc.issue.IssueJdbcTestData.*;

public class EpicJdbcServiceTest extends AbstractJdbcServiceTest {

    @Autowired
    public IssueService<Epic> issueEpicService;

    @BeforeClass
    public static void setUp() {
        EpicJdbcRepositoryImpl epicJdbcRepository = new EpicJdbcRepositoryImpl();
        epicJdbcRepository.clear();
    }

    @Test
    public void save() {
        Epic epic = issueEpicService.save(EPIC1);

        assertNotNull(epic);

        epic.setName("New epic");

        int id = epic.getId();

        epic = issueEpicService.save(epic);

        assertEquals(id, (int) epic.getId());

        assertEquals("New epic", epic.getName());
    }

    @Test
    public void delete() {
        Epic epic = issueEpicService.save(EPIC2);

        assertNotNull(epic);

        assertTrue(issueEpicService.delete(epic.getId()));
    }

    @Test
    public void get() {
        Epic epic = issueEpicService.save(EPIC3);

        assertNotNull(epic);

        epic = issueEpicService.get(epic.getId());

        assertEquals(epic.getId(), issueEpicService.get(epic.getId()).getId());
    }

    @Test
    public void getByName() {
        Epic epic = issueEpicService.save(EPIC3);

        assertNotNull(epic);

        epic = issueEpicService.getByName(epic.getName());

        assertEquals(epic.getName(), issueEpicService.getByName(epic.getName()).getName());
    }

    @Test
    public void getAll() {
        Epic epic1 = new Epic(EPIC1);
        Epic epic2 = new Epic(EPIC2);
        Epic epic3 = new Epic(EPIC3);

        epic1 = issueEpicService.save(epic1);
        epic2 = issueEpicService.save(epic2);
        epic3 = issueEpicService.save(epic3);

        List<Epic> epics = issueEpicService.getAll();

        assertNotEquals(0, epics.size());

        epics.forEach(epic -> assertNotNull(epic.getId()));
    }
}
