package ru.vorobyev.tracker.to.issues;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.vorobyev.tracker.domain.issue.*;
import ru.vorobyev.tracker.domain.issue.workflow.WorkflowStatus;
import ru.vorobyev.tracker.domain.user.User;

import java.time.LocalDateTime;

@NoArgsConstructor
@Getter
@Setter
public class EpicTo {
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
    private Story rootStory;

    @JsonIgnoreProperties({"hibernateLazyInitializer"})
    private Task rootTask;

    private Integer backlog_id;

    private Integer sprint_id;

    public EpicTo(Epic epic) {
        if (!epic.isNew()) {
            this.id = epic.getId();
        }
        populate(epic);
    }

    private void populate(Epic epic) {
        this.priority = epic.getPriority();
        this.creationDate = epic.getCreationDate();
        this.name = epic.getName();
        this.executor = epic.getExecutor();
        this.reporter = epic.getReporter();
        this.status = epic.getStatus();
        this.rootBug = epic.getRootBug();
        this.rootStory = epic.getRootStory();
        this.rootTask = epic.getRootTask();
        this.backlog_id = epic.getBacklog() == null ? null : epic.getBacklog().getId();
        this.sprint_id = epic.getSprint() == null ? null : epic.getSprint().getId();
    }
}
