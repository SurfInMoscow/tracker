package ru.vorobyev.tracker.domain.project;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.vorobyev.tracker.domain.AbstractBaseEntity;
import ru.vorobyev.tracker.domain.issue.Bug;
import ru.vorobyev.tracker.domain.issue.Epic;
import ru.vorobyev.tracker.domain.issue.Story;
import ru.vorobyev.tracker.domain.issue.Task;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import java.util.Set;

@Entity
@NoArgsConstructor
@Getter
@Setter
public class Sprint extends AbstractBaseEntity implements ProjectIssues {

    @OneToMany(
            mappedBy = "sprint",
            fetch = FetchType.LAZY,
            cascade = CascadeType.PERSIST,
            orphanRemoval = true
    )
    private Set<Bug> bugs;

    @OneToMany(
            mappedBy = "sprint",
            fetch = FetchType.LAZY,
            cascade = CascadeType.PERSIST,
            orphanRemoval = true
    )
    private Set<Epic> epics;

    @OneToMany(
            mappedBy = "sprint",
            fetch = FetchType.LAZY,
            cascade = CascadeType.PERSIST,
            orphanRemoval = true
    )
    private Set<Story> stories;

    @OneToMany(
            mappedBy = "sprint",
            fetch = FetchType.LAZY,
            cascade = CascadeType.PERSIST,
            orphanRemoval = true
    )
    private Set<Task> tasks;

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
