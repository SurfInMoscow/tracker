package ru.vorobyev.tracker.domain.issue;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import ru.vorobyev.tracker.domain.issue.workflow.WorkflowStatus;
import ru.vorobyev.tracker.domain.project.Backlog;
import ru.vorobyev.tracker.domain.project.Sprint;
import ru.vorobyev.tracker.domain.user.User;

import javax.persistence.*;
import java.time.LocalDateTime;

@NamedQueries({
        @NamedQuery(name = Bug.DELETE, query = "DELETE FROM Bug b WHERE b.id=:id"),
        @NamedQuery(name = Bug.GET_BY_NAME, query = "SELECT b FROM Bug b WHERE b.name=:name"),
        @NamedQuery(name = Bug.GET_ALL, query = "SELECT b FROM Bug b ORDER BY b.creationDate")
})
@Entity
@Table(name = "bugs")
@NoArgsConstructor
@Getter
@Setter
public class Bug extends AbstractIssue {

    public static final String DELETE = "bug.delete";
    public static final String GET_BY_NAME = "bug.getByName";
    public static final String GET_ALL = "bug.getAll";

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "root_epic_id")
    private Epic rootEpic;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "root_story_id")
    private Story rootStory;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "root_task_id")
    private Task rootTask;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "backlog_id")
    @ToString.Exclude
    private Backlog backlog;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sprint_id")
    @ToString.Exclude
    private Sprint sprint;

    public Bug(Issue issue) {
        super(issue);
    }

    public Bug(Priority priority, LocalDateTime creationDate, String name, User executor, User reporter, WorkflowStatus status) {
        super(priority, creationDate, name, executor, reporter, status);
    }
}
