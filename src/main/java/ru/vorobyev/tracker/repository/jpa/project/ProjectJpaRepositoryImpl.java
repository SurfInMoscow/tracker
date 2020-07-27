package ru.vorobyev.tracker.repository.jpa.project;

import ru.vorobyev.tracker.domain.project.Project;
import ru.vorobyev.tracker.repository.ProjectRepository;

import java.util.List;

public class ProjectJpaRepositoryImpl implements ProjectRepository {
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

    public void clear() {

    }
}
