package ru.vorobyev.tracker.domain.issue;

import lombok.*;
import ru.vorobyev.tracker.domain.AbstractBaseEntity;
import ru.vorobyev.tracker.domain.issue.workflow.WorkflowStatus;
import ru.vorobyev.tracker.domain.project.Backlog;
import ru.vorobyev.tracker.domain.project.Sprint;
import ru.vorobyev.tracker.domain.user.User;

import java.time.LocalDateTime;

@NoArgsConstructor
@Getter
@Setter
@ToString
public abstract class AbstractIssue extends AbstractBaseEntity implements Issue {
    private Priority priority;

    private LocalDateTime creationDate;

    private String name;

    @ToString.Exclude
    private User executor;

    @ToString.Exclude
    private User reporter;

    private WorkflowStatus status;

    @ToString.Exclude
    private Backlog backlog;

    @ToString.Exclude
    private Sprint sprint;

    public AbstractIssue(Issue issue) {
        this(null, issue.getPriority(), issue.getCreationDate(), issue.getName(), issue.getExecutor(), issue.getReporter(), issue.getStatus());
    }

    public AbstractIssue(Priority priority, LocalDateTime creationDate, String name, User executor, User reporter, WorkflowStatus status) {
        this(null, priority, creationDate, name, executor, reporter, status);
    }

    public AbstractIssue(Integer id, Priority priority, LocalDateTime creationDate, String name, User executor, User reporter, WorkflowStatus status) {
        super(id);
        this.priority = priority;
        this.creationDate = creationDate;
        this.name = name;
        this.executor = executor;
        this.reporter = reporter;
        this.status = status;
    }
}
