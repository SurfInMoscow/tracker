package ru.vorobyev.tracker.service.jdbc.issue;

import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import ru.vorobyev.tracker.AbstractJdbcServiceTest;
import ru.vorobyev.tracker.domain.issue.Story;
import ru.vorobyev.tracker.repository.jdbc.issue.StoryJdbcRepositoryImpl;
import ru.vorobyev.tracker.service.IssueService;

import java.util.List;

import static org.junit.Assert.*;
import static ru.vorobyev.tracker.service.jdbc.issue.IssueJdbcTestData.*;


public class StoryJdbcServiceTest extends AbstractJdbcServiceTest {

    @Autowired
    public IssueService<Story> issueStoryService;

    @BeforeClass
    public static void setUp() {
        StoryJdbcRepositoryImpl storyJdbcRepository = new StoryJdbcRepositoryImpl();
        storyJdbcRepository.clear();
    }

    @Test
    public void save() {
        Story story = issueStoryService.save(STORY1);

        assertNotNull(story);

        story.setName("New story");

        int id = story.getId();

        story = issueStoryService.save(story);

        assertEquals(id, (int) story.getId());

        assertEquals("New story", story.getName());
    }

    @Test
    public void delete() {
        Story story = issueStoryService.save(STORY2);

        assertNotNull(story);

        assertTrue(issueStoryService.delete(story.getId()));
    }

    @Test
    public void get() {
        Story story = issueStoryService.save(STORY3);

        assertNotNull(story);

        story = issueStoryService.get(story.getId());

        assertEquals(story.getId(), issueStoryService.get(story.getId()).getId());
    }

    @Test
    public void getByName() {
        Story story = issueStoryService.save(STORY3);

        assertNotNull(story);

        story = issueStoryService.getByName(story.getName());

        assertEquals(story.getName(), issueStoryService.getByName(story.getName()).getName());
    }

    @Test
    public void getAll() {
        Story story1 = new Story(STORY1);
        Story story2 = new Story(STORY2);
        Story story3 = new Story(STORY3);

        story1 = issueStoryService.save(story1);
        story2 = issueStoryService.save(story2);
        story3 = issueStoryService.save(story3);

        List<Story> storys = issueStoryService.getAll();

        assertNotEquals(0, storys.size());

        storys.forEach(story -> assertNotNull(story.getId()));
    }
}
