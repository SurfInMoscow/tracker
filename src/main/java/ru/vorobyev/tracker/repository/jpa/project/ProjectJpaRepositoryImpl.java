package ru.vorobyev.tracker.repository.jpa.project;

import org.springframework.context.annotation.Profile;
import org.springframework.dao.support.DataAccessUtils;
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

    @Transactional
    @Override
    public Project save(Project project) {
        if (project.isNew()) {
            em.persist(project);
            return project;
        } else {
            return em.merge(project);
        }
    }

    @Transactional
    @Override
    public boolean delete(int id) {
        return em.createNamedQuery(Project.DELETE)
                .setParameter("id", id)
                .executeUpdate() != 0;
    }

    @Override
    public Project get(int id) {
        return em.find(Project.class, id);
    }

    @Override
    public Project getByName(String name) {
        List<Project> projects = em.createNamedQuery(Project.GET_BY_NAME, Project.class)
                .setParameter("name", name)
                .getResultList();

        return DataAccessUtils.singleResult(projects);
    }

    @Override
    public List<Project> getAll() {
        return em.createNamedQuery(Project.GET_ALL, Project.class)
                .getResultList();
    }

    public void refresh(Project project) {
        em.refresh(project);
    }
}