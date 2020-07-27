package ru.vorobyev.tracker.repository.jpa.project;

import ru.vorobyev.tracker.domain.project.Backlog;
import ru.vorobyev.tracker.repository.BacklogRepository;

import java.time.LocalDateTime;
import java.util.List;

public class BacklogJpaRepositoryImpl implements BacklogRepository {
    @Override
    public Backlog save(Backlog backlog) {
        return null;
    }

    @Override
    public boolean delete(int id) {
        return false;
    }

    @Override
    public Backlog get(int id) {
        return null;
    }

    @Override
    public List<Backlog> getAll() {
        return null;
    }

    @Override
    public Backlog getWithIssuesByPrioriTy(int id, String priority) {
        return null;
    }

    @Override
    public Backlog getWithIssuesBetweenDates(int id, LocalDateTime startDate, LocalDateTime endDate) {
        return null;
    }

    @Override
    public Backlog getWithIssuesByName(int id, String name) {
        return null;
    }

    @Override
    public Backlog getWithIssuesByExecutor(int id, int executor_id) {
        return null;
    }

    @Override
    public Backlog getWithIssuesByReporter(int id, int reporter_id) {
        return null;
    }
}
