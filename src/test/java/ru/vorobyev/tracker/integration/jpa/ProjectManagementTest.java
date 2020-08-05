package ru.vorobyev.tracker.integration.jpa;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import ru.vorobyev.tracker.AbstractJpaServiceTest;
import ru.vorobyev.tracker.domain.issue.Bug;
import ru.vorobyev.tracker.domain.issue.Epic;
import ru.vorobyev.tracker.domain.issue.Story;
import ru.vorobyev.tracker.domain.issue.Task;
import ru.vorobyev.tracker.domain.issue.workflow.WorkflowStatus;
import ru.vorobyev.tracker.domain.project.Backlog;
import ru.vorobyev.tracker.domain.project.Project;
import ru.vorobyev.tracker.domain.project.Sprint;
import ru.vorobyev.tracker.domain.user.User;
import ru.vorobyev.tracker.service.*;

import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.*;
import static ru.vorobyev.tracker.integration.jpa.IssueTestData.*;
import static ru.vorobyev.tracker.integration.jpa.ProjectTestData.*;
import static ru.vorobyev.tracker.integration.jpa.UserTestData.*;


public class ProjectManagementTest extends AbstractJpaServiceTest {

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

        backlog.getTasks().addAll(Set.of(task1, task2, task3));

        backlog = backlogService.save(backlog);

        backlog = backlogService.get(backlog.getId());

        assertEquals(3, backlog.getEpics().size());
        assertEquals(3, backlog.getStories().size());
        assertEquals(3, backlog.getTasks().size());

        backlog.getEpics().forEach(epic -> assertNotNull(epic.getBacklog()));
        backlog.getStories().forEach(story -> assertNotNull(story.getBacklog()));
        backlog.getTasks().forEach(task -> assertNotNull(task.getBacklog()));
    }

    @Test
    public void moveIssueFromBacklogToSprint() {
        Backlog backlog = backlogService.save(BACKLOG2);

        Sprint sprint = sprintService.save(SPRINT2);

        assertNotNull(backlog.getId());

        assertNotNull(sprint.getId());

        Epic epic1 = new Epic(EPIC1);
        epic1.setBacklog(backlog);
        epicIssueService.save(epic1);
        Epic epic2 = new Epic(EPIC2);
        epic2.setBacklog(backlog);
        epicIssueService.save(epic2);
        Epic epic3 = new Epic(EPIC3);
        epic3.setBacklog(backlog);
        epicIssueService.save(epic3);

        Story story1 = new Story(STORY1);
        story1.setBacklog(backlog);
        storyIssueService.save(story1);
        Story story2 = new Story(STORY2);
        story2.setBacklog(backlog);
        storyIssueService.save(story2);

        backlog = backlogService.get(backlog.getId());

        backlog.getStories().forEach(story -> {
            Story tmpStory = storyIssueService.get(story.getId());
            tmpStory.setBacklog(null);
            tmpStory.setSprint(sprint);
            storyIssueService.save(tmpStory);
        });

        assertEquals(0, backlogService.get(backlog.getId()).getStories().size());
        assertEquals(2, sprintService.get(sprint.getId()).getStories().size());
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
