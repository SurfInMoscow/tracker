package ru.vorobyev.tracker.repository.jpa.issue;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Profile;
import org.springframework.dao.support.DataAccessUtils;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.vorobyev.tracker.domain.issue.Story;
import ru.vorobyev.tracker.repository.IssueRepository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Profile("jpa")
@Repository
@Qualifier("StoryRepository")
@Transactional(readOnly = true)
public class StoryJpaRepositoryImpl implements IssueRepository<Story> {

    @PersistenceContext
    private EntityManager em;

    @Transactional
    @Override
    public Story save(Story story) {
        if (story.isNew()) {
            em.persist(story);
            return story;
        } else {
            return em.merge(story);
        }
    }

    @Transactional
    @Override
    public boolean delete(int id) {
        return em.createNamedQuery(Story.DELETE)
                .setParameter("id", id)
                .executeUpdate() != 0;
    }

    @Override
    public Story get(int id) {
        return em.find(Story.class, id);
    }

    @Override
    public Story getByName(String name) {
        List<Story> stories = em.createNamedQuery(Story.GET_BY_NAME, Story.class)
                .setParameter("name", name)
                .getResultList();

        return DataAccessUtils.singleResult(stories);
    }

    @Override
    public List<Story> getAll() {
        return em.createNamedQuery(Story.GET_ALL, Story.class)
                .getResultList();
    }

    public void refresh(Story story) {
        em.refresh(story);
    }
}
