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
public class StoryTo {
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
    private Task rootTask;

    private Integer backlog_id;

    private Integer sprint_id;

    public StoryTo(Story story) {
        if (!story.isNew()) {
            this.id = story.getId();
        }
        populate(story);
    }

    private void populate(Story story) {
        this.priority = story.getPriority();
        this.creationDate = story.getCreationDate();
        this.name = story.getName();
        this.executor = story.getExecutor();
        this.reporter = story.getReporter();
        this.status = story.getStatus();
        this.rootBug = story.getRootBug();
        this.rootEpic = story.getRootEpic();
        this.rootTask = story.getRootTask();
        this.backlog_id = story.getBacklog() == null ? null : story.getBacklog().getId();
        this.sprint_id = story.getSprint() == null ? null : story.getSprint().getId();
    }
}
