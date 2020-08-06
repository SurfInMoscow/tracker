package ru.vorobyev.tracker.service.project;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.vorobyev.tracker.domain.project.Backlog;
import ru.vorobyev.tracker.repository.BacklogRepository;
import ru.vorobyev.tracker.service.BacklogService;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Slf4j
public class BacklogServiceImpl implements BacklogService {

    private BacklogRepository backlogRepository;

    @Autowired
    public BacklogServiceImpl(BacklogRepository backlogRepository) {
        this.backlogRepository = backlogRepository;
    }

    @Override
    public Backlog save(Backlog backlog) {
        log.info(backlog.toString() + " created.");
        return backlogRepository.save(backlog);
    }

    @Override
    public boolean delete(int id) {
        log.info(String.format("Backlog with id:%d deleted.", id));
        return backlogRepository.delete(id);
    }

    @Override
    public Backlog get(int id) {
        log.info(String.format("Get backlog with id:%d.", id));
        return backlogRepository.get(id);
    }

    @Override
    public List<Backlog> getAll() {
        log.info("Get all backlogs action.");
        return backlogRepository.getAll();
    }

    public Backlog getWithIssuesByPrioriTy(int id, String priority) {
        log.info(String.format("Get filtered backlog by priority:%s.", priority));
        return backlogRepository.getWithIssuesByPrioriTy(id, priority);
    }

    public Backlog getWithIssuesBetweenDates(int id, LocalDateTime startDate, LocalDateTime endDate) {
        log.info(String.format("Get filtered backlog between dates from:%s to:%s.", startDate.toString(), endDate.toString()));
        return backlogRepository.getWithIssuesBetweenDates(id, startDate, endDate);
    }

    public Backlog getWithIssuesByName(int id, String name) {
        log.info(String.format("Get filtered backlog by name:%s.", name));
        return backlogRepository.getWithIssuesByName(id, name);
    }

    public Backlog getWithIssuesByExecutor(int id, int executor_id) {
        log.info(String.format("Get filtered backlog by executorID:%d.", executor_id));
        return backlogRepository.getWithIssuesByExecutor(id, executor_id);
    }

    public Backlog getWithIssuesByReporter(int id, int reporter_id) {
        log.info(String.format("Get filtered backlog by reporterID:%d.", reporter_id));
        return backlogRepository.getWithIssuesByReporter(id, reporter_id);
    }
}
