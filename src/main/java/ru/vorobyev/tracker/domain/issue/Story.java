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
        @NamedQuery(name = Story.DELETE,query = "DELETE FROM Story s WHERE s.id=:id"),
        @NamedQuery(name = Story.GET_BY_NAME, query = "SELECT s FROM Story s WHERE s.name=:name"),
        @NamedQuery(name = Story.GET_ALL, query = "SELECT s FROM Story s ORDER BY s.creationDate")
})
@Entity
@Table(name = "stories")
@NoArgsConstructor
@Getter
@Setter
public class Story extends AbstractIssue {

    public static final String DELETE = "story.delete";
    public static final String GET_BY_NAME = "story.getByName";
    public static final String GET_ALL = "story.getAll";

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "root_bug_id")
    private Bug rootBug;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "root_epic_id")
    private Epic rootEpic;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "root_task_id")
    private Task rootTask;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "backlog_id")
    private Backlog backlog;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sprint_id")
    private Sprint sprint;

    public Story(Issue issue) {
        super(issue);
    }

    public Story(Priority priority, LocalDateTime creationDate, String name, User executor, User reporter, WorkflowStatus status) {
        super(priority, creationDate, name, executor, reporter, status);
    }
}
