package ru.vorobyev.tracker.repository;

import ru.vorobyev.tracker.domain.project.Backlog;

import java.util.List;

public interface BacklogRepository {
    Backlog save(Backlog backlog);

    boolean delete(int id);

    Backlog get(int id);

    List<Backlog> getAll();
}
