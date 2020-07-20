package ru.vorobyev.tracker.repository.inmemory.project;

import lombok.Getter;
import ru.vorobyev.tracker.domain.project.Backlog;
import ru.vorobyev.tracker.repository.BacklogRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static ru.vorobyev.tracker.repository.inmemory.SeqGenerator.BUG_SEQ_GENERATOR;

@Getter
public class BacklogRepositoryImpl implements BacklogRepository {

    private Map<Integer, Backlog> backlogRepo;

    public BacklogRepositoryImpl() {
        backlogRepo = new HashMap<>();
    }

    @Override
    public Backlog save(Backlog backlog) {
        if (backlog.isNew()) {
            backlog.setId(BUG_SEQ_GENERATOR.incrementAndGet());
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

    @Override
    public Backlog getWithIssuesByPrioriTy(int id, String priority) {
        Backlog backlog = backlogRepo.get(id);
        backlog.setBugs(backlog.getBugs().stream().filter(b -> b.getPriority().name().equals(priority)).collect(Collectors.toSet()));
        backlog.setEpics(backlog.getEpics().stream().filter(e -> e.getPriority().name().equals(priority)).collect(Collectors.toSet()));
        backlog.setStories(backlog.getStories().stream().filter(s -> s.getPriority().name().equals(priority)).collect(Collectors.toSet()));
        backlog.setTasks(backlog.getTasks().stream().filter(t -> t.getPriority().name().equals(priority)).collect(Collectors.toSet()));
        return backlog;
    }

    @Override
    public Backlog getWithIssuesBetweenDates(int id, LocalDateTime startDate, LocalDateTime endDate) {
        return null;
    }

    @Override
    public Backlog getWithIssuesByName(int id, String name) {
        return null;
    }

    @Override
    public Backlog getWithIssuesByExecutor(int id, int executor_id) {
        return null;
    }

    @Override
    public Backlog getWithIssuesByReporter(int id, int reporter_id) {
        return null;
    }
}
