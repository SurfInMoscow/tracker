package ru.vorobyev.tracker.service;

import ru.vorobyev.tracker.domain.project.Sprint;

import java.util.List;

public interface SprintService {
    Sprint save(Sprint sprint);

    boolean delete(int id);

    Sprint get(int id);

    List<Sprint> getAll();
}
