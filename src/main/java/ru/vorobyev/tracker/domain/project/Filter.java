package ru.vorobyev.tracker.domain.project;

import ru.vorobyev.tracker.domain.issue.Issue;
import ru.vorobyev.tracker.domain.issue.Priority;
import ru.vorobyev.tracker.domain.user.User;

import java.time.LocalDateTime;
import java.util.List;

public interface Filter {
    List<Issue> getByPriority(Priority priority);

    List<Issue> getByCreationDate(LocalDateTime localDateTime);

    List<Issue> getByName(String name);

    List<Issue> getByExecutor(User user);

    List<Issue> getByReporter(User user);
}
