package ru.vorobyev.tracker.repository.jpa.issue;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Profile;
import org.springframework.dao.support.DataAccessUtils;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.vorobyev.tracker.domain.issue.Bug;
import ru.vorobyev.tracker.repository.IssueRepository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Transactional(readOnly = true)
@Profile("jpa")
@Repository
@Qualifier("BugRepository")
public class BugJpaRepositoryImpl implements IssueRepository<Bug> {

    @PersistenceContext
    private EntityManager em;

    @Transactional
    @Override
    public Bug save(Bug bug) {
        if (bug.isNew()) {
            em.persist(bug);
            return bug;
        } else {
            return em.merge(bug);
        }
    }

    @Transactional
    @Override
    public boolean delete(int id) {
        return em.createNamedQuery(Bug.DELETE)
                .setParameter("id", id)
                .executeUpdate() != 0;
    }

    @Override
    public Bug get(int id) {
        return em.find(Bug.class, id);
    }

    @Override
    public Bug getByName(String name) {
        List<Bug> bugs = em.createNamedQuery(Bug.GET_BY_NAME, Bug.class)
                .setParameter("name", name)
                .getResultList();
        return DataAccessUtils.singleResult(bugs);
    }

    @Override
    public List<Bug> getAll() {
        return em.createNamedQuery(Bug.GET_ALL, Bug.class)
                .getResultList();
    }

    public void refresh(Bug bug) {
        em.refresh(bug);
    }
}
