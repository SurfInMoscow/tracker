package ru.vorobyev.tracker.repository.inmemory.project;

import lombok.Getter;
import ru.vorobyev.tracker.domain.project.Project;
import ru.vorobyev.tracker.repository.ProjectRepository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static ru.vorobyev.tracker.repository.inmemory.SeqGenerator.BUG_SEQ_GENERATOR;

@Getter
public class ProjectRepositoryImpl implements ProjectRepository {

    private Map<Integer, Project> projectRepo;

    public ProjectRepositoryImpl() {
        projectRepo = new HashMap<>();
    }

    @Override
    public Project save(Project project) {
        if (project.isNew()) {
            project.setId(BUG_SEQ_GENERATOR.incrementAndGet());
            projectRepo.put(project.getId(), project);
            return project;
        }

        return projectRepo.computeIfPresent(project.getId(), (key, value) -> value = project);
    }

    @Override
    public boolean delete(int id) {
        return projectRepo.remove(id) != null;
    }

    @Override
    public Project get(int id) {
        return projectRepo.get(id);
    }

    @Override
    public Project getByName(String name) {
        return projectRepo.values().stream().filter(project -> project.getName().equals(name)).findFirst().get();
    }

    @Override
    public List<Project> getAll() {
        return new ArrayList<>(projectRepo.values());
    }
}
