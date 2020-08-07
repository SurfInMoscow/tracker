package ru.vorobyev.tracker.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import ru.vorobyev.tracker.domain.issue.*;
import ru.vorobyev.tracker.domain.project.Backlog;
import ru.vorobyev.tracker.domain.project.Sprint;
import ru.vorobyev.tracker.domain.user.User;
import ru.vorobyev.tracker.exception.IllegalRequestException;
import ru.vorobyev.tracker.service.BacklogService;
import ru.vorobyev.tracker.service.IssueService;
import ru.vorobyev.tracker.service.SprintService;
import ru.vorobyev.tracker.service.UserService;
import ru.vorobyev.tracker.to.issues.*;

import java.net.URI;
import java.util.Objects;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static ru.vorobyev.tracker.api.IssuesRestController.REST_URL;

@RestController
@RequestMapping(value = REST_URL, produces = APPLICATION_JSON_VALUE)
public class IssuesRestController {

    protected static final String REST_URL = "/api/issues";

    private final IssueService<Bug> bugService;
    private final IssueService<Epic> epicService;
    private final IssueService<Story> storyService;
    private final IssueService<Task> taskService;
    private final BacklogService backlogService;
    private final SprintService sprintService;
    private final UserService userService;

    @Autowired
    public IssuesRestController(IssueService<Bug> bugService, IssueService<Epic> epicService,
                                IssueService<Story> storyService, IssueService<Task> taskService,
                                BacklogService backlogService, SprintService sprintService,
                                UserService userService) {
        this.bugService = bugService;
        this.epicService = epicService;
        this.storyService = storyService;
        this.taskService = taskService;
        this.backlogService = backlogService;
        this.sprintService = sprintService;
        this.userService = userService;
    }

    @GetMapping("/bug&id={id}")
    public BugTo getBug(@PathVariable int id) {
        return new BugTo(bugService.get(id));
    }

    @GetMapping("/epic&id={id}")
    public EpicTo getEpic(@PathVariable int id) {
        return new EpicTo(epicService.get(id));
    }

    @GetMapping("/story&id={id}")
    public StoryTo getStory(@PathVariable int id) {
        return new StoryTo(storyService.get(id));
    }

    @GetMapping("/task&id={id}")
    public TaskTo getTask(@PathVariable int id) {
        return new TaskTo(taskService.get(id));
    }

    @PostMapping(value = "/{type}", consumes = APPLICATION_JSON_VALUE)
    public ResponseEntity<AbstractIssue> saveIssue(@RequestBody CommonIssueTo commonIssueTo, @PathVariable String type) {
        return responseFromType(commonIssueTo, type);
    }

    @DeleteMapping("/{type}&id={id}")
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void deleteIssue(@PathVariable String type, @PathVariable int id) {
        Objects.requireNonNull(type, "Type not specified.");

        switch (type) {
            case "bug":
                bugService.delete(id);
                break;
            case "epic":
                epicService.delete(id);
                break;
            case "story":
                storyService.delete(id);
                break;
            case "task":
                taskService.delete(id);
                break;
        }
    }

    @PutMapping(value = "/bug&id={id}", consumes = APPLICATION_JSON_VALUE)
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void updateBug(@RequestBody BugTo bug, @PathVariable int id) {
        if (bug.getId() == null || bug.getId() != id)
            throw new IllegalRequestException("Bug should be with id:" + id);

        Bug tmp = bugService.get(id);
        tmp.setName(bug.getName());
        tmp.setPriority(bug.getPriority());
        tmp.setStatus(bug.getStatus());
        tmp.setCreationDate(bug.getCreationDate());
        tmp.setExecutor(bug.getExecutor());
        tmp.setReporter(bug.getReporter());
        tmp.setRootEpic(bug.getRootEpic());
        tmp.setRootStory(bug.getRootStory());
        tmp.setRootTask(bug.getRootTask());
        Backlog backlog = bug.getBacklog_id() == null ? null : backlogService.get(bug.getBacklog_id());
        Sprint sprint = bug.getSprint_id() == null ? null : sprintService.get(bug.getSprint_id());
        tmp.setBacklog(backlog);
        tmp.setSprint(sprint);
        bugService.save(tmp);
    }

    @PutMapping(value = "/epic&id={id}", consumes = APPLICATION_JSON_VALUE)
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void updateEpic(@RequestBody EpicTo epic, @PathVariable int id) {
        if (epic.getId() == null || epic.getId() != id)
            throw new IllegalRequestException("Epic should be with id:" + id);

        Epic tmp = epicService.get(id);
        tmp.setName(epic.getName());
        tmp.setPriority(epic.getPriority());
        tmp.setStatus(epic.getStatus());
        tmp.setCreationDate(epic.getCreationDate());
        tmp.setExecutor(epic.getExecutor());
        tmp.setReporter(epic.getReporter());
        tmp.setRootBug(epic.getRootBug());
        tmp.setRootStory(epic.getRootStory());
        tmp.setRootTask(epic.getRootTask());
        Backlog backlog = epic.getBacklog_id() == null ? null : backlogService.get(epic.getBacklog_id());
        Sprint sprint = epic.getSprint_id() == null ? null : sprintService.get(epic.getSprint_id());
        tmp.setBacklog(backlog);
        tmp.setSprint(sprint);
        epicService.save(tmp);
    }

    @PutMapping(value = "/story&id={id}", consumes = APPLICATION_JSON_VALUE)
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void updateStory(@RequestBody StoryTo story, @PathVariable int id) {
        if (story.getId() == null || story.getId() != id)
            throw new IllegalRequestException("Story should be with id:" + id);

        Story tmp = storyService.get(id);
        tmp.setName(story.getName());
        tmp.setPriority(story.getPriority());
        tmp.setStatus(story.getStatus());
        tmp.setCreationDate(story.getCreationDate());
        tmp.setExecutor(story.getExecutor());
        tmp.setReporter(story.getReporter());
        tmp.setRootBug(story.getRootBug());
        tmp.setRootEpic(story.getRootEpic());
        tmp.setRootTask(story.getRootTask());
        Backlog backlog = story.getBacklog_id() == null ? null : backlogService.get(story.getBacklog_id());
        Sprint sprint = story.getSprint_id() == null ? null : sprintService.get(story.getSprint_id());
        tmp.setBacklog(backlog);
        tmp.setSprint(sprint);
        storyService.save(tmp);
    }

    @PutMapping(value = "/task&id={id}", consumes = APPLICATION_JSON_VALUE)
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void updateTask(@RequestBody TaskTo task, @PathVariable int id) {
        if (task.getId() == null || task.getId() != id)
            throw new IllegalRequestException("Task should be with id:" + id);

        Task tmp = taskService.get(id);
        tmp.setName(task.getName());
        tmp.setPriority(task.getPriority());
        tmp.setStatus(task.getStatus());
        tmp.setCreationDate(task.getCreationDate());
        tmp.setExecutor(task.getExecutor());
        tmp.setReporter(task.getReporter());
        tmp.setRootBug(task.getRootBug());
        tmp.setRootEpic(task.getRootEpic());
        tmp.setRootStory(task.getRootStory());
        Backlog backlog = task.getBacklog_id() == null ? null : backlogService.get(task.getBacklog_id());
        Sprint sprint = task.getSprint_id() == null ? null : sprintService.get(task.getSprint_id());
        tmp.setBacklog(backlog);
        tmp.setSprint(sprint);
        taskService.save(tmp);
    }

    private ResponseEntity<AbstractIssue> responseFromType(CommonIssueTo commonIssueTo, String type) {
        User reporter = commonIssueTo.getReporter_id() == null ? null : userService.get(commonIssueTo.getReporter_id());
        User executor = commonIssueTo.getExecutor_id() == null ? null : userService.get(commonIssueTo.getExecutor_id());
        Backlog backlog = commonIssueTo.getBacklog_id() == null ? null : backlogService.get(commonIssueTo.getBacklog_id());
        Sprint sprint = commonIssueTo.getSprint_id() == null ? null : sprintService.get(commonIssueTo.getSprint_id());
        URI uri;

        switch (type) {
            case "bug":
                Bug bug = new Bug(commonIssueTo.getPriority(), commonIssueTo.getCreationDate(), commonIssueTo.getName(), executor, reporter, commonIssueTo.getStatus());
                bug.setBacklog(backlog);
                bug.setSprint(sprint);
                bug = bugService.save(bug);
                uri = ServletUriComponentsBuilder.fromCurrentContextPath().path(REST_URL + "/bug&id={id}").buildAndExpand(bug.getId()).toUri();
                return ResponseEntity.created(uri).body(bug);
            case "epic":
                Epic epic = new Epic(commonIssueTo.getPriority(), commonIssueTo.getCreationDate(), commonIssueTo.getName(), executor, reporter, commonIssueTo.getStatus());
                epic.setBacklog(backlog);
                epic.setSprint(sprint);
                epic =  epicService.save(epic);
                uri = ServletUriComponentsBuilder.fromCurrentContextPath().path(REST_URL + "/epic&id={id}").buildAndExpand(epic.getId()).toUri();
                return ResponseEntity.created(uri).body(epic);
            case "story":
                Story story = new Story(commonIssueTo.getPriority(), commonIssueTo.getCreationDate(), commonIssueTo.getName(), executor, reporter, commonIssueTo.getStatus());
                story.setBacklog(backlog);
                story.setSprint(sprint);
                story = storyService.save(story);
                uri = ServletUriComponentsBuilder.fromCurrentContextPath().path(REST_URL + "/story&id={id}").buildAndExpand(story.getId()).toUri();
                return ResponseEntity.created(uri).body(story);
            case "task":
                Task task = new Task(commonIssueTo.getPriority(), commonIssueTo.getCreationDate(), commonIssueTo.getName(), executor, reporter, commonIssueTo.getStatus());
                task.setBacklog(backlog);
                task.setSprint(sprint);
                task = taskService.save(task);
                uri = ServletUriComponentsBuilder.fromCurrentContextPath().path(REST_URL + "/task&id={id}").buildAndExpand(task.getId()).toUri();
                return ResponseEntity.created(uri).body(task);
        }

        return null;
    }
}