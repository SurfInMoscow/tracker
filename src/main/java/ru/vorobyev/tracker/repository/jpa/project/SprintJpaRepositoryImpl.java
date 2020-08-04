package ru.vorobyev.tracker.repository.jpa.project;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.vorobyev.tracker.domain.project.Sprint;
import ru.vorobyev.tracker.repository.SprintRepository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Profile("jpa")
@Repository
@Transactional(readOnly = true)
public class SprintJpaRepositoryImpl implements SprintRepository {

    @PersistenceContext
    private EntityManager em;

    @Transactional
    @Override
    public Sprint save(Sprint sprint) {
        if (sprint.isNew()) {
            em.persist(sprint);
            return sprint;
        } else {
            return em.merge(sprint);
        }
    }

    @Transactional
    @Override
    public boolean delete(int id) {
        return em.createNamedQuery(Sprint.DELETE)
                .setParameter("id", id)
                .executeUpdate() != 0;
    }

    @Override
    public Sprint get(int id) {
        return em.find(Sprint.class, id);
    }

    @Override
    public List<Sprint> getAll() {
        return em.createNamedQuery(Sprint.GET_ALL, Sprint.class)
                .getResultList();
    }

    public void refresh(Sprint sprint) {
        em.refresh(sprint);
    }
}
