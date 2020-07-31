package ru.vorobyev.tracker.repository.jpa.issue;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Profile;
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

    @Override
    public Epic save(Epic epic) {
        return null;
    }

    @Override
    public boolean delete(int id) {
        return false;
    }

    @Override
    public Epic get(int id) {
        return null;
    }

    @Override
    public Epic getByName(String name) {
        return null;
    }

    @Override
    public List<Epic> getAll() {
        return null;
    }

    public void refresh() {}
}
