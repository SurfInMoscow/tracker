package ru.vorobyev.tracker.repository.inmemory.project;

import lombok.Getter;
import ru.vorobyev.tracker.domain.project.Backlog;
import ru.vorobyev.tracker.repository.BacklogRepository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static ru.vorobyev.tracker.repository.inmemory.SeqGenerator.SEQ_GENERATOR;

@Getter
public class BacklogRepositoryImpl implements BacklogRepository {

    private Map<Integer, Backlog> backlogRepo;

    public BacklogRepositoryImpl() {
        backlogRepo = new HashMap<>();
    }

    @Override
    public Backlog save(Backlog backlog) {
        if (backlog.isNew()) {
            backlog.setId(SEQ_GENERATOR.incrementAndGet());
            backlogRepo.put(backlog.getId(), backlog);
            return backlog;
        }

        return backlogRepo.computeIfPresent(backlog.getId(), (key, value) -> value = backlog);
    }

    @Override
    public boolean delete(int id) {
        return backlogRepo.remove(id) != null;
    }

    @Override
    public Backlog get(int id) {
        return backlogRepo.get(id);
    }

    @Override
    public List<Backlog> getAll() {
        return new ArrayList<>(backlogRepo.values());
    }
}
