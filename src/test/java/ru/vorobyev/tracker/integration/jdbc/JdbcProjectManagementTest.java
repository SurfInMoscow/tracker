package ru.vorobyev.tracker.integration.jdbc;

import org.junit.BeforeClass;
import org.junit.Test;
import ru.vorobyev.tracker.domain.issue.Bug;
import ru.vorobyev.tracker.domain.issue.Epic;
import ru.vorobyev.tracker.domain.issue.Story;
import ru.vorobyev.tracker.domain.issue.Task;
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
import ru.vorobyev.tracker.service.issue.BugServiceImpl;
import ru.vorobyev.tracker.service.issue.EpicServiceImpl;
import ru.vorobyev.tracker.service.issue.StoryServiceImpl;
import ru.vorobyev.tracker.service.issue.TaskServiceImpl;
import ru.vorobyev.tracker.service.project.BacklogServiceImpl;
import ru.vorobyev.tracker.service.project.ProjectServiceImpl;
import ru.vorobyev.tracker.service.project.SprintServiceImpl;
import ru.vorobyev.tracker.service.user.UserServiceImpl;

import javax.validation.constraints.NotNull;
import java.util.HashSet;

import static org.junit.Assert.*;
import static ru.vorobyev.tracker.integration.jdbc.IssueTestData.*;
import static ru.vorobyev.tracker.integration.jdbc.ProjectTestData.*;
import static ru.vorobyev.tracker.integration.jdbc.UserTestData.*;


public class JdbcProjectManagementTest {

    private static UserService userService;

    private static ProjectService projectService;

    private static BacklogService backlogService;

    private static SprintService sprintService;

    private static IssueService<Bug> bugIssueService;

    private static IssueService<Epic> epicIssueService;

    private static IssueService<Story> storyIssueService;

    private static IssueService<Task> taskIssueService;

    @BeforeClass
    public static void setUp() {
        UserJdbcRepositoryImpl userRepository = new UserJdbcRepositoryImpl();
        userService = new UserServiceImpl(userRepository);
        userRepository.clear();
        ProjectJdbcRepositoryImpl projectRepository = new ProjectJdbcRepositoryImpl();
        projectService = new ProjectServiceImpl(projectRepository);
        projectRepository.clear();
        BacklogJdbcRepositoryImpl backlogRepository = new BacklogJdbcRepositoryImpl();
        backlogService = new BacklogServiceImpl(backlogRepository);
        backlogRepository.clear();
        SprintJdbcRepositoryImpl sprintRepository = new SprintJdbcRepositoryImpl();
        sprintService = new SprintServiceImpl(sprintRepository);
        sprintRepository.clear();
        StoryJdbcRepositoryImpl storyRepository = new StoryJdbcRepositoryImpl();
        storyIssueService = new StoryServiceImpl(storyRepository);
        storyRepository.clear();
        BugJdbcRepositoryImpl bugRepository = new BugJdbcRepositoryImpl();
        bugIssueService = new BugServiceImpl(bugRepository);
        bugRepository.clear();
        EpicJdbcRepositoryImpl epicRepository = new EpicJdbcRepositoryImpl();
        epicIssueService = new EpicServiceImpl(epicRepository);
        epicRepository.clear();
        TaskJdbcRepositoryImpl taskRepository = new TaskJdbcRepositoryImpl();
        taskIssueService = new TaskServiceImpl(taskRepository);
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
    public void changeWorkflowStatus() {
        Bug bug = bugIssueService.save(new Bug(BUG1));

        assertNotNull(bug.getId());

        WorkflowStatus oldStatus = bug.getStatus();

        bug.setStatus(WorkflowStatus.REVIEW_ISSUE);

        bug = bugIssueService.save(bug);

        assertNotEquals(oldStatus, bug.getStatus());
    }
}
