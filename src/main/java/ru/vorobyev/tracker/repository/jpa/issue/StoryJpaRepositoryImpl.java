package ru.vorobyev.tracker.repository.jpa.issue;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.vorobyev.tracker.domain.issue.Story;
import ru.vorobyev.tracker.repository.IssueRepository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Profile("jpa")
@Repository
@Transactional(readOnly = true)
public class StoryJpaRepositoryImpl implements IssueRepository<Story> {

    @PersistenceContext
    private EntityManager em;

    @Override
    public Story save(Story issue) {
        return null;
    }

    @Override
    public boolean delete(int id) {
        return false;
    }

    @Override
    public Story get(int id) {
        return null;
    }

    @Override
    public Story getByName(String name) {
        return null;
    }

    @Override
    public List<Story> getAll() {
        return null;
    }

    public void refresh() {}
}
