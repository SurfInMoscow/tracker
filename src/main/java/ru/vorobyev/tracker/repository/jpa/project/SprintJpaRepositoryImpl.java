package ru.vorobyev.tracker.repository.jpa.project;

import ru.vorobyev.tracker.domain.project.Sprint;
import ru.vorobyev.tracker.repository.SprintRepository;

import java.util.List;

public class SprintJpaRepositoryImpl implements SprintRepository {
    @Override
    public Sprint save(Sprint sprint) {
        return null;
    }

    @Override
    public boolean delete(int id) {
        return false;
    }

    @Override
    public Sprint get(int id) {
        return null;
    }

    @Override
    public List<Sprint> getAll() {
        return null;
    }

    public void clear() {}
}
