package ru.vorobyev.tracker.domain.project;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.vorobyev.tracker.domain.AbstractBaseEntity;
import ru.vorobyev.tracker.domain.issue.*;
import ru.vorobyev.tracker.domain.user.User;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Set;

@NoArgsConstructor
@Getter
@Setter
public class Backlog extends AbstractBaseEntity implements ProjectIssues, Filter {
    private Set<Bug> bugs;

    private Set<Epic> epics;

    private Set<Story> stories;

    private Set<Task> tasks;

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

    @Override
    public List<Issue> getByPriority(Priority priority) {
        Objects.requireNonNull(priority);
        //return issues.stream().filter(issue -> issue.getPriority().equals(priority)).collect(Collectors.toList());
        return null;
    }

    @Override
    public List<Issue> getByCreationDate(LocalDateTime localDateTime) {
        Objects.requireNonNull(localDateTime);
        //return issues.stream().filter(issue -> issue.getCreationDate().equals(localDateTime)).collect(Collectors.toList());
        return null;
    }

    @Override
    public List<Issue> getByName(String name) {
        Objects.requireNonNull(name);
        //return issues.stream().filter(issue -> issue.getName().equals(name)).collect(Collectors.toList());
        return null;
    }

    @Override
    public List<Issue> getByExecutor(User user) {
        Objects.requireNonNull(user);
        //return issues.stream().filter(issue -> issue.getExecutor().equals(user)).collect(Collectors.toList());
        return null;
    }

    @Override
    public List<Issue> getByReporter(User user) {
        Objects.requireNonNull(user);
        //return issues.stream().filter(issue -> issue.getReporter().equals(user)).collect(Collectors.toList());
        return null;
    }
}
