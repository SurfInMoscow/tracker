package ru.vorobyev.tracker.service;

import ru.vorobyev.tracker.domain.project.Backlog;

import java.util.List;

public interface BacklogService {
    Backlog save(Backlog backlog);

    boolean delete(int id);

    Backlog get(int id);

    List<Backlog> getAll();
}
