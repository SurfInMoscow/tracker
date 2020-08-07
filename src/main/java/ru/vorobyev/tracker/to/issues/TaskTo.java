package ru.vorobyev.tracker.to.issues;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;
import ru.vorobyev.tracker.domain.issue.*;
import ru.vorobyev.tracker.domain.issue.workflow.WorkflowStatus;
import ru.vorobyev.tracker.domain.user.User;

import java.time.LocalDateTime;

@Getter
@Setter
public class TaskTo {
    private Integer id;

    private Priority priority;

    private LocalDateTime creationDate;

    private String name;

    @JsonIgnoreProperties({"hibernateLazyInitializer"})
    private User executor;

    @JsonIgnoreProperties({"hibernateLazyInitializer"})
    private User reporter;

    private WorkflowStatus status;

    @JsonIgnoreProperties({"hibernateLazyInitializer"})
    private Bug rootBug;

    @JsonIgnoreProperties({"hibernateLazyInitializer"})
    private Epic rootEpic;

    @JsonIgnoreProperties({"hibernateLazyInitializer"})
    private Story rootStory;

    private Integer backlog_id;

    private Integer sprint_id;

    public TaskTo(Task task) {
        if (!task.isNew()) {
            this.id = task.getId();
        }
        populate(task);
    }

    private void populate(Task task) {
        this.priority = task.getPriority();
        this.creationDate = task.getCreationDate();
        this.name = task.getName();
        this.executor = task.getExecutor();
        this.reporter = task.getReporter();
        this.status = task.getStatus();
        this.rootBug = task.getRootBug();
        this.rootEpic = task.getRootEpic();
        this.rootStory = task.getRootStory();
        this.backlog_id = task.getBacklog() == null ? null : task.getBacklog().getId();
        this.sprint_id = task.getSprint() == null ? null : task.getSprint().getId();
    }
}
