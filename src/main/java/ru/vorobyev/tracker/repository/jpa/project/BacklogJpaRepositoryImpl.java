package ru.vorobyev.tracker.repository.jpa.project;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.vorobyev.tracker.domain.project.Backlog;
import ru.vorobyev.tracker.repository.BacklogRepository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.time.LocalDateTime;
import java.util.List;

@Profile("jpa")
@Repository
@Transactional(readOnly = true)
public class BacklogJpaRepositoryImpl implements BacklogRepository {

    @PersistenceContext
    private EntityManager em;

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

    public void refresh() {}
}
