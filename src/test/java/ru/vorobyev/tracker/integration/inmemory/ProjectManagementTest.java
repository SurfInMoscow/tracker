package ru.vorobyev.tracker.integration.inmemory;

import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import ru.vorobyev.tracker.domain.issue.Bug;
import ru.vorobyev.tracker.domain.issue.Epic;
import ru.vorobyev.tracker.domain.issue.Story;
import ru.vorobyev.tracker.domain.issue.Task;
import ru.vorobyev.tracker.domain.project.Backlog;
import ru.vorobyev.tracker.domain.project.Project;
import ru.vorobyev.tracker.domain.project.Sprint;
import ru.vorobyev.tracker.domain.user.User;
import ru.vorobyev.tracker.repository.inmemory.issue.BugRepositoryImpl;
import ru.vorobyev.tracker.repository.inmemory.issue.EpicRepositoryImpl;
import ru.vorobyev.tracker.repository.inmemory.issue.StoryRepositoryImpl;
import ru.vorobyev.tracker.repository.inmemory.issue.TaskRepositoryImpl;
import ru.vorobyev.tracker.repository.inmemory.project.BacklogRepositoryImpl;
import ru.vorobyev.tracker.repository.inmemory.project.ProjectRepositoryImpl;
import ru.vorobyev.tracker.repository.inmemory.project.SprintRepositoryImpl;
import ru.vorobyev.tracker.repository.inmemory.user.UserRepositoryImpl;
import ru.vorobyev.tracker.service.*;
import ru.vorobyev.tracker.service.issue.BugServiceImpl;
import ru.vorobyev.tracker.service.issue.EpicServiceImpl;
import ru.vorobyev.tracker.service.issue.StoryServiceImpl;
import ru.vorobyev.tracker.service.issue.TaskServiceImpl;
import ru.vorobyev.tracker.service.project.BacklogServiceImpl;
import ru.vorobyev.tracker.service.project.ProjectServiceImpl;
import ru.vorobyev.tracker.service.project.SprintServiceImpl;
import ru.vorobyev.tracker.service.user.UserServiceImpl;

import static org.junit.Assert.*;
import static ru.vorobyev.tracker.domain.issue.workflow.WorkflowStatus.RESOLVED_ISSUE;
import static ru.vorobyev.tracker.integration.inmemory.IssueTestData.TASK1;
import static ru.vorobyev.tracker.integration.inmemory.ProjectTestData.PROJECT1;
import static ru.vorobyev.tracker.integration.inmemory.UserTestData.*;


public class ProjectManagementTest {

    private static UserService userService;

    private static ProjectService projectService;

    private static BacklogService backlogService;

    private static SprintService sprintService;

    private static IssueService<Bug> bugService;

    private static IssueService<Epic> epicService;

    private static IssueService<Story> storyService;

    private static IssueService<Task> taskService;

    @BeforeClass
    public static void setUp() {
        userService = new UserServiceImpl(new UserRepositoryImpl(), new BCryptPasswordEncoder());
        projectService = new ProjectServiceImpl(new ProjectRepositoryImpl());
        backlogService = new BacklogServiceImpl(new BacklogRepositoryImpl());
        sprintService = new SprintServiceImpl(new SprintRepositoryImpl());
        bugService = new BugServiceImpl(new BugRepositoryImpl());
        epicService = new EpicServiceImpl(new EpicRepositoryImpl());
        storyService = new StoryServiceImpl(new StoryRepositoryImpl());
        taskService = new TaskServiceImpl(new TaskRepositoryImpl());
    }

    @Test
    public void createProjectSpace() {
        User manager = userService.save(USER1);
        User admin = userService.save(USER2);

        PROJECT1.setAdministrator(admin.getName());
        PROJECT1.setManager(manager.getName());

        Project project = projectService.save(PROJECT1);

        manager.getProjects().add(project);
        admin.getProjects().add(project);
        userService.save(manager);
        userService.save(admin);

        assertEquals(userService.get(admin.getId()).getName(), projectService.get(project.getId()).getAdministrator());

        assertTrue(userService.get(manager.getId()).getProjects().contains(projectService.get(project.getId())));
    }

    @Test
    public void addIssueToBackLog() {
        Project project = projectService.save(PROJECT1);

        Backlog backlog = backlogService.save(project.getBacklog());

        Task task = taskService.save(TASK1);

        project.getBacklog().getTasks().add(task);

        assertEquals(projectService.get(project.getId()).getBacklog().getId(), backlogService.get(backlog.getId()).getId());

        assertTrue(backlogService.get(backlog.getId()).getTasks().contains(taskService.get(task.getId())));
    }

    @Test
    public void moveIssueFromBacklogToSprint() {
        Project project = projectService.save(PROJECT1);

        Backlog backlog = backlogService.save(project.getBacklog());

        Sprint sprint = sprintService.save(project.getSprint());

        Task task = taskService.save(TASK1);

        project.getBacklog().getTasks().add(task);

        assertEquals(projectService.get(project.getId()).getBacklog().getId(), backlogService.get(backlog.getId()).getId());

        int taskID = task.getId();

        task = project.getBacklog().getTasks().stream().filter(tsk -> tsk.getId().equals(taskID)).findFirst().get();

        project.getBacklog().getTasks().remove(task);

        sprint.getTasks().add(task);

        assertTrue(sprint.getTasks().contains(task));

        assertFalse(backlogService.get(backlog.getId()).getTasks().contains(task));

        assertEquals(project.getSprint().getId(), sprintService.get(sprint.getId()).getId());
    }

    @Test
    public void changeWorkflowStatus() {
        Project project = projectService.save(PROJECT1);

        Backlog backlog = backlogService.save(project.getBacklog());

        Task task = taskService.save(TASK1);

        project.getBacklog().getTasks().add(task);

        task.setStatus(RESOLVED_ISSUE);

        assertEquals(project.getBacklog().getTasks().stream()
                .filter(tsk -> tsk.getStatus().equals(RESOLVED_ISSUE))
                .findFirst().get(), task);
    }
}
