package ru.vorobyev.tracker.repository;

import ru.vorobyev.tracker.domain.project.Sprint;

import java.util.List;

public interface SprintRepository {
    Sprint save(Sprint sprint);

    boolean delete(int id);

    Sprint get(int id);

    List<Sprint> getAll();
}
