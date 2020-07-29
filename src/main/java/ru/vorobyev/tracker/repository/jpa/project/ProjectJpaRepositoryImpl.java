package ru.vorobyev.tracker.repository.jpa.project;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.vorobyev.tracker.domain.project.Project;
import ru.vorobyev.tracker.repository.ProjectRepository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Profile("jpa")
@Repository
@Transactional(readOnly = true)
public class ProjectJpaRepositoryImpl implements ProjectRepository {

    @PersistenceContext
    private EntityManager em;

    @Override
    public Project save(Project project) {
        return null;
    }

    @Override
    public boolean delete(int id) {
        return false;
    }

    @Override
    public Project get(int id) {
        return null;
    }

    @Override
    public Project getByName(String name) {
        return null;
    }

    @Override
    public List<Project> getAll() {
        return null;
    }

    public void refresh() {

    }
}
