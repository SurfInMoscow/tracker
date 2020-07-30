package ru.vorobyev.tracker.repository.jpa.issue;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.vorobyev.tracker.domain.issue.Bug;
import ru.vorobyev.tracker.repository.IssueRepository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Profile("jpa")
@Repository
@Transactional(readOnly = true)
public class BugJpaRepositoryImpl implements IssueRepository<Bug> {

    @PersistenceContext
    private EntityManager em;

    @Override
    public Bug save(Bug bug) {
        return null;
    }

    @Override
    public boolean delete(int id) {
        return false;
    }

    @Override
    public Bug get(int id) {
        return null;
    }

    @Override
    public Bug getByName(String name) {
        return null;
    }

    @Override
    public List<Bug> getAll() {
        return null;
    }

    public void refresh() {}
}
