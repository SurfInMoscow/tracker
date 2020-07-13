package ru.vorobyev.tracker.repository;

import ru.vorobyev.tracker.domain.project.Project;

import java.util.List;

public interface ProjectRepository {
    Project save(Project project);

    boolean delete(int id);

    Project get(int id);

    Project getByName(String name);

    List<Project> getAll();
}
