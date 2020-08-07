package ru.vorobyev.tracker.domain.issue;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import ru.vorobyev.tracker.domain.AbstractBaseEntity;
import ru.vorobyev.tracker.domain.issue.workflow.WorkflowStatus;
import ru.vorobyev.tracker.domain.user.User;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

@MappedSuperclass
@NoArgsConstructor
@Getter
@Setter
@ToString
public abstract class AbstractIssue extends AbstractBaseEntity implements Issue {
    @Column(name = "priority")
    @NotNull
    @Enumerated(EnumType.STRING)
    private Priority priority;

    @Column(name = "creation_date")
    @NotNull
    private LocalDateTime creationDate;

    @Column(name = "name")
    @NotNull
    @Size(min = 2, max = 100)
    private String name;

    @JsonIgnore
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "executor_id")
    @ToString.Exclude
    private User executor;

    @JsonIgnore
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reporter_id")
    @ToString.Exclude
    private User reporter;

    @Column(name = "status")
    @NotNull
    @Enumerated(EnumType.STRING)
    private WorkflowStatus status;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;

        AbstractIssue that = (AbstractIssue) o;

        if (priority != that.priority) return false;
        if (!creationDate.equals(that.creationDate)) return false;
        if (!name.equals(that.name)) return false;
        return status == that.status;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + priority.hashCode();
        result = 31 * result + creationDate.hashCode();
        result = 31 * result + name.hashCode();
        result = 31 * result + status.hashCode();
        return result;
    }
}