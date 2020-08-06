package ru.vorobyev.tracker.domain.issue;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.BatchSize;
import ru.vorobyev.tracker.domain.issue.workflow.WorkflowStatus;
import ru.vorobyev.tracker.domain.project.Backlog;
import ru.vorobyev.tracker.domain.project.Sprint;
import ru.vorobyev.tracker.domain.user.User;

import javax.persistence.*;
import java.time.LocalDateTime;

@NamedQueries({
        @NamedQuery(name = Epic.DELETE, query = "DELETE FROM Epic e WHERE e.id=:id"),
        @NamedQuery(name = Epic.GET_BY_NAME, query = "SELECT e FROM Epic e WHERE e.name=:name"),
        @NamedQuery(name = Epic.GET_ALL, query = "SELECT e FROM Epic e ORDER BY e.creationDate")
})
@Entity
@Table(name = "epics")
@NoArgsConstructor
@Getter
@Setter
public class Epic extends AbstractIssue {

    public static final String DELETE = "epic.delete";
    public static final String GET_BY_NAME = "epic.getByName";
    public static final String GET_ALL = "epic.getAll";

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "root_bug_id")
    private Bug rootBug;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "root_story_id")
    private Story rootStory;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "root_task_id")
    private Task rootTask;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "backlog_id")
    private Backlog backlog;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sprint_id")
    private Sprint sprint;

    public Epic(Issue issue) {
        super(issue);
    }

    public Epic(Priority priority, LocalDateTime creationDate, String name, User executor, User reporter, WorkflowStatus status) {
        super(priority, creationDate, name, executor, reporter, status);
    }
}
