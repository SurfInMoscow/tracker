package ru.vorobyev.tracker.service.project;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.vorobyev.tracker.domain.project.Project;
import ru.vorobyev.tracker.repository.ProjectRepository;
import ru.vorobyev.tracker.service.ProjectService;

import java.util.List;

@Service
public class ProjectServiceImpl implements ProjectService {

    private ProjectRepository projectRepository;

    @Autowired
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
