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
        @NamedQuery(name = Task.DELETE, query = "DELETE FROM Task t WHERE t.id=:id"),
        @NamedQuery(name = Task.GET_BY_NAME, query = "SELECT t FROM Task t WHERE t.name=:name"),
        @NamedQuery(name = Task.GET_ALL, query = "SELECT t FROM Task t ORDER BY t.creationDate")
})
@Entity
@Table(name = "tasks")
@NoArgsConstructor
@Getter
@Setter
public class Task extends AbstractIssue {

    public static final String DELETE = "task.delete";
    public static final String GET_BY_NAME = "task.getByName";
    public static final String GET_ALL = "task.getAll";

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "root_bug_id")
    private Bug rootBug;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "root_epic_id")
    private Epic rootEpic;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "root_story_id")
    private Story rootStory;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "backlog_id")
    private Backlog backlog;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sprint_id")
    private Sprint sprint;

    public Task(Issue issue) {
        super(issue);
    }

    public Task(Priority priority, LocalDateTime creationDate, String name, User executor, User reporter, WorkflowStatus status) {
        super(priority, creationDate, name, executor, reporter, status);
    }
}
