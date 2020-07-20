package ru.vorobyev.tracker.service;

import ru.vorobyev.tracker.domain.project.Backlog;

import java.time.LocalDateTime;
import java.util.List;

public interface BacklogService {
    Backlog save(Backlog backlog);

    boolean delete(int id);

    Backlog get(int id);

    List<Backlog> getAll();

    Backlog getWithIssuesByPrioriTy(int id, String priority);

    Backlog getWithIssuesBetweenDates(int id, LocalDateTime startDate, LocalDateTime endDate);

    Backlog getWithIssuesByName(int id, String name);

    Backlog getWithIssuesByExecutor(int id, int executor_id);

    Backlog getWithIssuesByReporter(int id, int reporter_id);
}
