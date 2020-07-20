package ru.vorobyev.tracker.service.project;

import ru.vorobyev.tracker.domain.project.Backlog;
import ru.vorobyev.tracker.repository.BacklogRepository;
import ru.vorobyev.tracker.service.BacklogService;

import java.time.LocalDateTime;
import java.util.List;

public class BacklogServiceImpl implements BacklogService {

    private BacklogRepository backlogRepository;

    public BacklogServiceImpl(BacklogRepository backlogRepository) {
        this.backlogRepository = backlogRepository;
    }

    @Override
    public Backlog save(Backlog backlog) {
        return backlogRepository.save(backlog);
    }

    @Override
    public boolean delete(int id) {
        return backlogRepository.delete(id);
    }

    @Override
    public Backlog get(int id) {
        return backlogRepository.get(id);
    }

    @Override
    public List<Backlog> getAll() {
        return backlogRepository.getAll();
    }

    public Backlog getWithIssuesByPrioriTy(int id, String priority) {
        return backlogRepository.getWithIssuesByPrioriTy(id, priority);
    }

    public Backlog getWithIssuesBetweenDates(int id, LocalDateTime startDate, LocalDateTime endDate) {
        return backlogRepository.getWithIssuesBetweenDates(id, startDate, endDate);
    }

    public Backlog getWithIssuesByName(int id, String name) {
        return backlogRepository.getWithIssuesByName(id, name);
    }

    public Backlog getWithIssuesByExecutor(int id, int executor_id) {
        return backlogRepository.getWithIssuesByExecutor(id, executor_id);
    }

    public Backlog getWithIssuesByReporter(int id, int reporter_id) {
        return backlogRepository.getWithIssuesByReporter(id, reporter_id);
    }
}
