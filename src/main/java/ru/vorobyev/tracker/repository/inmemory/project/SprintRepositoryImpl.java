package ru.vorobyev.tracker.repository.inmemory.project;

import ru.vorobyev.tracker.domain.project.Sprint;
import ru.vorobyev.tracker.repository.SprintRepository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static ru.vorobyev.tracker.repository.inmemory.SeqGenerator.BUG_SEQ_GENERATOR;

public class SprintRepositoryImpl implements SprintRepository {

    private Map<Integer, Sprint> sprintRepo;

    public SprintRepositoryImpl() {
        sprintRepo = new HashMap<>();
    }

    @Override
    public Sprint save(Sprint sprint) {
        if (sprint.isNew()) {
            sprint.setId(BUG_SEQ_GENERATOR.incrementAndGet());
            sprintRepo.put(sprint.getId(), sprint);
            return sprint;
        }

        return sprintRepo.computeIfPresent(sprint.getId(), (key, value) -> value = sprint);
    }

    @Override
    public boolean delete(int id) {
        return sprintRepo.remove(id) != null;
    }

    @Override
    public Sprint get(int id) {
        return sprintRepo.get(id);
    }

    @Override
    public List<Sprint> getAll() {
        return new ArrayList<>(sprintRepo.values());
    }
}
