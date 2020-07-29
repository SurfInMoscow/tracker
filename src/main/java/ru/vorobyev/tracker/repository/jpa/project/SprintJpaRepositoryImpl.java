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

    @Override
    public Sprint save(Sprint sprint) {
        return null;
    }

    @Override
    public boolean delete(int id) {
        return false;
    }

    @Override
    public Sprint get(int id) {
        return null;
    }

    @Override
    public List<Sprint> getAll() {
        return null;
    }

    public void refresh() {}
}
