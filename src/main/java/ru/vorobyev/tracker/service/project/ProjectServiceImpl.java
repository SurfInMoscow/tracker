package ru.vorobyev.tracker.service.project;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.vorobyev.tracker.domain.project.Project;
import ru.vorobyev.tracker.repository.ProjectRepository;
import ru.vorobyev.tracker.service.ProjectService;

import java.util.List;

@Service
@Slf4j
public class ProjectServiceImpl implements ProjectService {

    private ProjectRepository projectRepository;

    @Autowired
    public ProjectServiceImpl(ProjectRepository projectRepository) {
        this.projectRepository = projectRepository;
    }

    @Override
    public Project save(Project project) {
        log.info(project.toString() + " created.");
        return projectRepository.save(project);
    }

    @Override
    public boolean delete(int id) {
        log.info(String.format("Project with id:%d deleted.", id));
        return projectRepository.delete(id);
    }

    @Override
    public Project get(int id) {
        log.info(String.format("Get project with id:%d", id));
        return projectRepository.get(id);
    }

    @Override
    public Project getByName(String name) {
        log.info(String.format("Get project with name:%s", name));
        return projectRepository.getByName(name);
    }

    @Override
    public List<Project> getAll() {
        log.info("Get all projects action.");
        return projectRepository.getAll();
    }
}
