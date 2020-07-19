package ru.vorobyev.tracker.service.inmemory.issue;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import ru.vorobyev.tracker.domain.issue.Story;
import ru.vorobyev.tracker.repository.inmemory.issue.StoryRepositoryImpl;
import ru.vorobyev.tracker.service.IssueService;
import ru.vorobyev.tracker.service.issue.StoryServiceImpl;

import java.util.List;

import static org.junit.Assert.*;

public class StoryServiceImplTest {

    private static IssueService<Story> storyService;

    /*Sequence для ID стартует со 100*/

    @BeforeClass
    public static void setUp() {
        storyService = new StoryServiceImpl(new StoryRepositoryImpl());
        storyService.save(IssueTestData.STORY1);
        storyService.save(IssueTestData.STORY2);
    }

    @Test
    public void save() {
        storyService.save(IssueTestData.STORY3);

        Story tmpStory = storyService.get(103);

        assertNotNull(tmpStory);

        tmpStory.setName("New Name");

        storyService.save(tmpStory);

        assertEquals(tmpStory, storyService.get(103));
    }

    @Test
    public void delete() {
        assertTrue(storyService.delete(101));
    }

    @Test
    public void get() {
        assertNotNull(storyService.get(102));
    }

    @Test
    public void getByName() {
        Story tmpStory = storyService.getByName("Second story");

        assertEquals("Second story", tmpStory.getName());
    }

    @Test
    public void getAll() {
        List<Story> stories = storyService.getAll();

        assertNotSame(0, stories.size());

        stories.forEach(Assert::assertNotNull);
    }
}