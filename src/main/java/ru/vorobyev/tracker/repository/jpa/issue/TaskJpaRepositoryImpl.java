package ru.vorobyev.tracker.repository.jpa.issue;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Profile;
import org.springframework.dao.support.DataAccessUtils;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.vorobyev.tracker.domain.issue.Task;
import ru.vorobyev.tracker.repository.IssueRepository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Profile("jpa")
@Repository
@Qualifier("TaskRepository")
@Transactional(readOnly = true)
public class TaskJpaRepositoryImpl implements IssueRepository<Task> {

    @PersistenceContext
    private EntityManager em;

    @Transactional
    @Override
    public Task save(Task task) {
        if (task.isNew()) {
            em.persist(task);
            return task;
        } else {
            return em.merge(task);
        }
    }

    @Transactional
    @Override
    public boolean delete(int id) {
        return em.createNamedQuery(Task.DELETE)
                .setParameter("id", id)
                .executeUpdate() != 0;
    }

    @Override
    public Task get(int id) {
        return em.find(Task.class, id);
    }

    @Override
    public Task getByName(String name) {
        List<Task> tasks = em.createNamedQuery(Task.GET_BY_NAME, Task.class)
                .setParameter("name", name)
                .getResultList();

        return DataAccessUtils.singleResult(tasks);
    }

    @Override
    public List<Task> getAll() {
        return em.createNamedQuery(Task.GET_ALL, Task.class)
                .getResultList();
    }

    public void refresh(Task task) {
        em.refresh(task);
    }
}
