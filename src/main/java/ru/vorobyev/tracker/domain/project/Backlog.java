package ru.vorobyev.tracker.domain.project;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.vorobyev.tracker.domain.AbstractBaseEntity;
import ru.vorobyev.tracker.domain.issue.Issue;
import ru.vorobyev.tracker.domain.issue.Priority;
import ru.vorobyev.tracker.domain.user.User;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@NoArgsConstructor
@Getter
@Setter
public class Backlog extends AbstractBaseEntity implements ProjectIssues, Filter {
    private List<Issue> issues;

    public Backlog(List<Issue> issues) {
        this(null, issues);
    }

    public Backlog(Integer id, List<Issue> issues) {
        super(id);
        this.issues = issues;
    }

    @Override
    public List<Issue> getByPriority(Priority priority) {
        Objects.requireNonNull(priority);
        return issues.stream().filter(issue -> issue.getPriority().equals(priority)).collect(Collectors.toList());
    }

    @Override
    public List<Issue> getByCreationDate(LocalDateTime localDateTime) {
        Objects.requireNonNull(localDateTime);
        return issues.stream().filter(issue -> issue.getCreationDate().equals(localDateTime)).collect(Collectors.toList());
    }

    @Override
    public List<Issue> getByName(String name) {
        Objects.requireNonNull(name);
        return issues.stream().filter(issue -> issue.getName().equals(name)).collect(Collectors.toList());
    }

    @Override
    public List<Issue> getByExecutor(User user) {
        Objects.requireNonNull(user);
        return issues.stream().filter(issue -> issue.getExecutor().equals(user)).collect(Collectors.toList());
    }

    @Override
    public List<Issue> getByReporter(User user) {
        Objects.requireNonNull(user);
        return issues.stream().filter(issue -> issue.getReporter().equals(user)).collect(Collectors.toList());
    }
}
