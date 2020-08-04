package ru.vorobyev.tracker.domain.project;

import lombok.Getter;
import lombok.Setter;
import ru.vorobyev.tracker.domain.AbstractBaseEntity;
import ru.vorobyev.tracker.domain.issue.Bug;
import ru.vorobyev.tracker.domain.issue.Epic;
import ru.vorobyev.tracker.domain.issue.Story;
import ru.vorobyev.tracker.domain.issue.Task;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@NamedQueries({
        @NamedQuery(name = Sprint.DELETE, query = "DELETE FROM Sprint s WHERE s.id=:id"),
        @NamedQuery(name = Sprint.GET_ALL, query = "SELECT s FROM Sprint s")
})
@Entity
@Getter
@Setter
public class Sprint extends AbstractBaseEntity implements ProjectIssues {

    public static final String DELETE = "sprint.delete";
    public static final String GET_ALL = "sprint.getAll";

    @OneToMany(
            mappedBy = "sprint",
            fetch = FetchType.LAZY,
            cascade = CascadeType.MERGE,
            orphanRemoval = true
    )
    private Set<Bug> bugs;

    @OneToMany(
            mappedBy = "sprint",
            fetch = FetchType.LAZY,
            cascade = CascadeType.MERGE,
            orphanRemoval = true
    )
    private Set<Epic> epics;

    @OneToMany(
            mappedBy = "sprint",
            fetch = FetchType.LAZY,
            cascade = CascadeType.MERGE,
            orphanRemoval = true
    )
    private Set<Story> stories;

    @OneToMany(
            mappedBy = "sprint",
            fetch = FetchType.LAZY,
            cascade = CascadeType.MERGE,
            orphanRemoval = true
    )
    private Set<Task> tasks;

    public Sprint() {
        this(new HashSet<>(), new HashSet<>(), new HashSet<>(), new HashSet<>());
    }

    public Sprint(Set<Bug> bugs, Set<Epic> epics, Set<Story> stories, Set<Task> tasks) {
        this(null, bugs, epics, stories, tasks);
    }

    public Sprint(Integer id, Set<Bug> bugs, Set<Epic> epics, Set<Story> stories, Set<Task> tasks) {
        super(id);
        this.bugs = bugs;
        this.epics = epics;
        this.stories = stories;
        this.tasks = tasks;
    }
}
