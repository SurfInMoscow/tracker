package ru.vorobyev.tracker.integration.jdbc;

import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import ru.vorobyev.tracker.AbstractJdbcServiceTest;
import ru.vorobyev.tracker.domain.issue.*;
import ru.vorobyev.tracker.domain.issue.workflow.WorkflowStatus;
import ru.vorobyev.tracker.domain.project.Backlog;
import ru.vorobyev.tracker.domain.project.Project;
import ru.vorobyev.tracker.domain.project.Sprint;
import ru.vorobyev.tracker.domain.user.User;
import ru.vorobyev.tracker.repository.jdbc.issue.BugJdbcRepositoryImpl;
import ru.vorobyev.tracker.repository.jdbc.issue.EpicJdbcRepositoryImpl;
import ru.vorobyev.tracker.repository.jdbc.issue.StoryJdbcRepositoryImpl;
import ru.vorobyev.tracker.repository.jdbc.issue.TaskJdbcRepositoryImpl;
import ru.vorobyev.tracker.repository.jdbc.project.BacklogJdbcRepositoryImpl;
import ru.vorobyev.tracker.repository.jdbc.project.ProjectJdbcRepositoryImpl;
import ru.vorobyev.tracker.repository.jdbc.project.SprintJdbcRepositoryImpl;
import ru.vorobyev.tracker.repository.jdbc.user.UserJdbcRepositoryImpl;
import ru.vorobyev.tracker.service.*;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.*;
import static ru.vorobyev.tracker.integration.jdbc.IssueTestData.*;
import static ru.vorobyev.tracker.integration.jdbc.ProjectTestData.*;
import static ru.vorobyev.tracker.integration.jdbc.UserTestData.*;


public class ProjectManagementTest extends AbstractJdbcServiceTest {

    @Autowired
    private UserService userService;

    @Autowired
    private ProjectService projectService;

    @Autowired
    private BacklogService backlogService;

    @Autowired
    private SprintService sprintService;

    @Autowired
    private IssueService<Bug> bugIssueService;

    @Autowired
    private IssueService<Epic> epicIssueService;

    @Autowired
    private IssueService<Story> storyIssueService;

    @Autowired
    private IssueService<Task> taskIssueService;

    @BeforeClass
    public static void setUp() {
        UserJdbcRepositoryImpl userRepository = new UserJdbcRepositoryImpl();
        userRepository.clear();
        ProjectJdbcRepositoryImpl projectRepository = new ProjectJdbcRepositoryImpl();
        projectRepository.clear();
        BacklogJdbcRepositoryImpl backlogRepository = new BacklogJdbcRepositoryImpl();
        backlogRepository.clear();
        SprintJdbcRepositoryImpl sprintRepository = new SprintJdbcRepositoryImpl();
        sprintRepository.clear();
        StoryJdbcRepositoryImpl storyRepository = new StoryJdbcRepositoryImpl();
        storyRepository.clear();
        BugJdbcRepositoryImpl bugRepository = new BugJdbcRepositoryImpl();
        bugRepository.clear();
        EpicJdbcRepositoryImpl epicRepository = new EpicJdbcRepositoryImpl();
        epicRepository.clear();
        TaskJdbcRepositoryImpl taskRepository = new TaskJdbcRepositoryImpl();
        taskRepository.clear();
    }

    @Test
    public void createProjectSpace() {
        User manager = userService.save(USER1);
        User admin = userService.save(USER2);
        User user = userService.save(USER3);

        PROJECT1.setAdministrator(admin.getName());
        PROJECT1.setManager(manager.getName());
        PROJECT1.setParticipants(new HashSet<>());
        PROJECT1.getParticipants().add(user);
        PROJECT1.getParticipants().add(manager);
        PROJECT1.getParticipants().add(admin);

        Project project = projectService.save(PROJECT1);

        manager.getProjects().add(project);
        admin.getProjects().add(project);
        user.getProjects().add(project);
        userService.save(manager);
        userService.save(admin);
        userService.save(user);

        project.getParticipants().forEach(usr -> assertTrue(userService.get(usr.getId()).
                                                            getProjects().contains(project)));
    }

    @Test
    public void addIssueToBackLog() {
        Backlog backlog = backlogService.save(BACKLOG1);

        assertNotNull(backlog.getId());

        Epic epic1 = new Epic(EPIC1);
        epic1.setBacklog(backlog);
        Epic epic2 = new Epic(EPIC2);
        epic2.setBacklog(backlog);
        Epic epic3 = new Epic(EPIC3);
        epic3.setBacklog(backlog);
        epic1 = epicIssueService.save(epic1);
        epic2 = epicIssueService.save(epic2);
        epic3 = epicIssueService.save(epic3);

        Story story1 = new Story(STORY1);
        story1.setBacklog(backlog);
        Story story2 = new Story(STORY2);
        story2.setBacklog(backlog);
        Story story3 = new Story(STORY3);
        story3.setBacklog(backlog);
        story1 = storyIssueService.save(story1);
        story2 = storyIssueService.save(story2);
        story3 = storyIssueService.save(story3);

        Task task1 = new Task(TASK1);
        task1.setBacklog(backlog);
        Task task2 = new Task(TASK2);
        task2.setBacklog(backlog);
        Task task3 = new Task(TASK3);
        task3.setBacklog(backlog);
        task1 = taskIssueService.save(task1);
        task2 = taskIssueService.save(task2);
        task3 = taskIssueService.save(task3);

        backlog = backlogService.get(backlog.getId());

        assertEquals(3, backlog.getEpics().size());
        assertEquals(3, backlog.getStories().size());
        assertEquals(3, backlog.getTasks().size());

        assertTrue(backlog.getTasks().contains(task3));
        assertTrue(backlog.getEpics().contains(epic3));
        assertTrue(backlog.getStories().contains(story3));
    }

    @Test
    public void moveIssueFromBacklogToSprint() {
        Backlog backlog = backlogService.save(BACKLOG2);

        Sprint sprint = sprintService.save(SPRINT2);

        assertNotNull(backlog.getId());

        assertNotNull(sprint.getId());

        Epic epic1 = new Epic(EPIC1);
        epic1.setBacklog(backlog);
        Epic epic2 = new Epic(EPIC2);
        epic2.setBacklog(backlog);
        Epic epic3 = new Epic(EPIC3);
        epic3.setBacklog(backlog);
        epic1 = epicIssueService.save(epic1);
        epic2 = epicIssueService.save(epic2);
        epic3 = epicIssueService.save(epic3);

        Story story1 = new Story(STORY1);
        story1.setBacklog(backlog);
        Story story2 = new Story(STORY2);
        story2.setBacklog(backlog);
        Story story3 = new Story(STORY3);
        story3.setBacklog(backlog);
        story1 = storyIssueService.save(story1);
        story2 = storyIssueService.save(story2);
        story3 = storyIssueService.save(story3);

        backlog = backlogService.get(backlog.getId());

        backlog.getStories().forEach(story -> {
            Story tmpStory = storyIssueService.get(story.getId());
            tmpStory.setBacklog(null);
            tmpStory.setSprint(sprint);
            storyIssueService.save(tmpStory);
        });

        assertEquals(0, backlogService.get(backlog.getId()).getStories().size());
        assertEquals(3, sprintService.get(sprint.getId()).getStories().size());

        story1 = storyIssueService.get(story1.getId());
        story1.setRootEpic(epic1);
        story1 = storyIssueService.save(story1);

        assertEquals(epic1.getId(), story1.getRootEpic().getId());
    }

    @Test
    public void filterBacklog() {
        Backlog backlog = backlogService.save(BACKLOG3);

        assertNotNull(backlog.getId());

        Bug bug1 = new Bug(BUG1);
        bug1.setBacklog(backlog);
        bug1.setCreationDate(LocalDateTime.of(2020, 5, 23, 19, 0));
        Bug bug2 = new Bug(BUG2);
        bug2.setBacklog(backlog);
        bug2.setCreationDate(LocalDateTime.of(2020, 6, 23, 19, 0));
        Bug bug3 = new Bug(BUG3);
        bug3.setBacklog(backlog);
        bug3.setCreationDate(LocalDateTime.of(2020, 7, 23, 19, 0));
        Epic epic1 = new Epic(EPIC1);
        epic1.setBacklog(backlog);
        epic1.setCreationDate(LocalDateTime.of(2020, 5, 23, 19, 0));
        Epic epic2 = new Epic(EPIC2);
        epic2.setBacklog(backlog);
        epic2.setCreationDate(LocalDateTime.of(2020, 6, 23, 19, 0));
        Epic epic3 = new Epic(EPIC3);
        epic3.setBacklog(backlog);
        epic3.setCreationDate(LocalDateTime.of(2020, 7, 23, 19, 0));
        Story story1 = new Story(STORY1);
        story1.setBacklog(backlog);
        story1.setCreationDate(LocalDateTime.of(2020, 5, 23, 19, 0));
        Story story2 = new Story(STORY2);
        story2.setBacklog(backlog);
        story2.setCreationDate(LocalDateTime.of(2020, 6, 23, 19, 0));
        Story story3 = new Story(STORY3);
        story3.setBacklog(backlog);
        story3.setCreationDate(LocalDateTime.of(2020, 7, 23, 19, 0));

        bugIssueService.save(bug1);
        bugIssueService.save(bug2);
        bugIssueService.save(bug3);
        epicIssueService.save(epic1);
        epicIssueService.save(epic2);
        epicIssueService.save(epic3);
        storyIssueService.save(story1);
        storyIssueService.save(story2);
        storyIssueService.save(story3);

        LocalDateTime start = LocalDateTime.of(2020, 5, 23, 21, 0);
        LocalDateTime end = LocalDateTime.of(2020, 6, 23, 23, 0);
        backlog = backlogService.getWithIssuesBetweenDates(backlog.getId(), start, end);

        Set<Issue> issues = new HashSet<>();
        issues.addAll(backlog.getBugs());
        issues.addAll(backlog.getEpics());
        issues.addAll(backlog.getStories());

        issues.forEach(i -> {assertTrue(i.getCreationDate().isAfter(start) && i.getCreationDate().isBefore(end));});
    }

    @Test
    public void changeWorkflowStatus() {
        Bug bug = bugIssueService.save(new Bug(BUG1));

        assertNotNull(bug.getId());

        WorkflowStatus oldStatus = bug.getStatus();

        bug.setStatus(WorkflowStatus.REVIEW_ISSUE);

        bug = bugIssueService.save(bug);

        assertNotEquals(oldStatus, bug.getStatus());
    }
}
