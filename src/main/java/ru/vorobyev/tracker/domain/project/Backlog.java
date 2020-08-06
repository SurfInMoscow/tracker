package ru.vorobyev.tracker.domain.project;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import ru.vorobyev.tracker.domain.AbstractBaseEntity;
import ru.vorobyev.tracker.domain.issue.Bug;
import ru.vorobyev.tracker.domain.issue.Epic;
import ru.vorobyev.tracker.domain.issue.Story;
import ru.vorobyev.tracker.domain.issue.Task;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@NamedQueries({
        @NamedQuery(name = Backlog.DELETE, query = "DELETE FROM Backlog b WHERE b.id=:id"),
        @NamedQuery(name = Backlog.GET_ALL, query = "SELECT b FROM Backlog b"),
})
@Entity
@Getter
@Setter
public class Backlog extends AbstractBaseEntity implements ProjectIssues {

    public static final String DELETE = "backlog.delete";
    public static final String GET_ALL = "backlog.getAll";
    public static final String GET_BY_PRIORITY = "backlog.getByPriority";

    @OneToMany(
            mappedBy = "backlog",
            fetch = FetchType.LAZY,
            cascade = CascadeType.MERGE,
            orphanRemoval = true
    )
    @ToString.Exclude
    private Set<Bug> bugs;

    @OneToMany(
            mappedBy = "backlog",
            fetch = FetchType.LAZY,
            cascade = CascadeType.MERGE,
            orphanRemoval = true
    )
    @ToString.Exclude
    private Set<Epic> epics;

    @OneToMany(
            mappedBy = "backlog",
            fetch = FetchType.LAZY,
            cascade = CascadeType.MERGE,
            orphanRemoval = true
    )
    @ToString.Exclude
    private Set<Story> stories;

    @OneToMany(
            mappedBy = "backlog",
            fetch = FetchType.LAZY,
            cascade = CascadeType.MERGE,
            orphanRemoval = true
    )
    @ToString.Exclude
    private Set<Task> tasks;

    public Backlog() {
        this(new HashSet<>(), new HashSet<>(), new HashSet<>(), new HashSet<>());
    }

    public Backlog(Set<Bug> bugs, Set<Epic> epics, Set<Story> stories, Set<Task> tasks) {
        this(null, bugs, epics, stories, tasks);
    }

    public Backlog(Integer id, Set<Bug> bugs, Set<Epic> epics, Set<Story> stories, Set<Task> tasks) {
        super(id);
        this.bugs = bugs;
        this.epics = epics;
        this.stories = stories;
        this.tasks = tasks;
    }
}
