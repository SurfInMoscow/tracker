package ru.vorobyev.tracker.service;

import ru.vorobyev.tracker.domain.project.Project;

import java.util.List;

public interface ProjectService {
    Project save(Project project);

    boolean delete(int id);

    Project get(int id);

    Project getByName(String name);

    List<Project> getAll();
}
