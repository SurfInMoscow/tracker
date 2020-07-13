package ru.vorobyev.tracker.service.project;

import ru.vorobyev.tracker.domain.project.Project;
import ru.vorobyev.tracker.repository.ProjectRepository;
import ru.vorobyev.tracker.service.ProjectService;

import java.util.List;

public class ProjectServiceImpl implements ProjectService {

    private ProjectRepository projectRepository;

    public ProjectServiceImpl(ProjectRepository projectRepository) {
        this.projectRepository = projectRepository;
    }

    @Override
    public Project save(Project project) {
        return projectRepository.save(project);
    }

    @Override
    public boolean delete(int id) {
        return projectRepository.delete(id);
    }

    @Override
    public Project get(int id) {
        return projectRepository.get(id);
    }

    @Override
    public Project getByName(String name) {
        return projectRepository.getByName(name);
    }

    @Override
    public List<Project> getAll() {
        return projectRepository.getAll();
    }
}
