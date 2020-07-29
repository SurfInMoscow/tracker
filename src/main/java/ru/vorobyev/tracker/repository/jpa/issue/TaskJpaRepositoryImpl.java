package ru.vorobyev.tracker.repository.jpa.issue;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.vorobyev.tracker.domain.issue.Task;
import ru.vorobyev.tracker.repository.IssueRepository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Profile("jpa")
@Repository
@Transactional(readOnly = true)
public class TaskJpaRepositoryImpl implements IssueRepository<Task> {

    @PersistenceContext
    private EntityManager em;

    @Override
    public Task save(Task issue) {
        return null;
    }

    @Override
    public boolean delete(int id) {
        return false;
    }

    @Override
    public Task get(int id) {
        return null;
    }

    @Override
    public Task getByName(String name) {
        return null;
    }

    @Override
    public List<Task> getAll() {
        return null;
    }

    public void refresh() {}
}
