package ru.vorobyev.tracker.repository.jpa.issue;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Profile;
import org.springframework.dao.support.DataAccessUtils;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.vorobyev.tracker.domain.issue.Epic;
import ru.vorobyev.tracker.repository.IssueRepository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Profile("jpa")
@Repository
@Qualifier("EpicRepository")
@Transactional(readOnly = true)
public class EpicJpaRepositoryImpl implements IssueRepository<Epic> {

    @PersistenceContext
    private EntityManager em;

    @Transactional
    @Override
    public Epic save(Epic epic) {
        if (epic.isNew()) {
            em.persist(epic);
            return epic;
        } else {
            return em.merge(epic);
        }
    }

    @Transactional
    @Override
    public boolean delete(int id) {
        return em.createNamedQuery(Epic.DELETE)
                .setParameter("id", id)
                .executeUpdate() != 0;
    }

    @Override
    public Epic get(int id) {
        return em.find(Epic.class, id);
    }

    @Override
    public Epic getByName(String name) {
        List<Epic> epics = em.createNamedQuery(Epic.GET_BY_NAME, Epic.class)
                .setParameter("name", name)
                .getResultList();

        return DataAccessUtils.singleResult(epics);
    }

    @Override
    public List<Epic> getAll() {
        return em.createNamedQuery(Epic.GET_ALL, Epic.class)
                .getResultList();
    }

    public void refresh(Epic epic) {
        em.refresh(epic);
    }
}
