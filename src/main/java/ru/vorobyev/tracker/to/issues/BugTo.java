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
public class BugTo {

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
    private Epic rootEpic;

    @JsonIgnoreProperties({"hibernateLazyInitializer"})
    private Story rootStory;

    @JsonIgnoreProperties({"hibernateLazyInitializer"})
    private Task rootTask;

    private Integer backlog_id;

    private Integer sprint_id;

    public BugTo(Bug bug) {
        if (!bug.isNew()) {
            this.id = bug.getId();
        }
        populate(bug);
    }

    private void populate(Bug bug) {
        this.priority = bug.getPriority();
        this.creationDate = bug.getCreationDate();
        this.name = bug.getName();
        this.executor = bug.getExecutor();
        this.reporter = bug.getReporter();
        this.status = bug.getStatus();
        this.rootEpic = bug.getRootEpic();
        this.rootStory = bug.getRootStory();
        this.rootTask = bug.getRootTask();
        this.backlog_id = bug.getBacklog() == null ? null : bug.getBacklog().getId();
        this.sprint_id = bug.getSprint() == null ? null : bug.getSprint().getId();
    }
}
