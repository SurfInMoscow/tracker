package ru.vorobyev.tracker.domain.project;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.vorobyev.tracker.domain.AbstractBaseEntity;
import ru.vorobyev.tracker.domain.issue.Bug;
import ru.vorobyev.tracker.domain.issue.Epic;
import ru.vorobyev.tracker.domain.issue.Story;
import ru.vorobyev.tracker.domain.issue.Task;

import java.util.Set;

@NoArgsConstructor
@Getter
@Setter
public class Sprint extends AbstractBaseEntity implements ProjectIssues {
    private Set<Bug> bugs;

    private Set<Epic> epics;

    private Set<Story> stories;

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
