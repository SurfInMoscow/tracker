package ru.vorobyev.tracker.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.vorobyev.tracker.domain.issue.*;
import ru.vorobyev.tracker.service.IssueService;
import ru.vorobyev.tracker.to.issues.BugTo;
import ru.vorobyev.tracker.to.issues.EpicTo;
import ru.vorobyev.tracker.to.issues.StoryTo;
import ru.vorobyev.tracker.to.issues.TaskTo;

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

    @Autowired
    public IssuesRestController(IssueService<Bug> bugService, IssueService<Epic> epicService,
                                IssueService<Story> storyService, IssueService<Task> taskService) {
        this.bugService = bugService;
        this.epicService = epicService;
        this.storyService = storyService;
        this.taskService = taskService;
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

    //TODO
    @PutMapping(value = "", consumes = APPLICATION_JSON_VALUE)
    public void updateIssue(@RequestBody Bug bug, @PathVariable boolean toSprint) {

    }
}