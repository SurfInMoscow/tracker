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
        Backlog backlog = backlogRepo.get(id);
        backlog.setBugs(backlog.getBugs().stream().filter(b -> b.getCreationDate().isAfter(startDate) && b.getCreationDate().isBefore(endDate)).collect(Collectors.toSet()));
        backlog.setEpics(backlog.getEpics().stream().filter(e -> e.getCreationDate().isAfter(startDate) && e.getCreationDate().isBefore(endDate)).collect(Collectors.toSet()));
        backlog.setStories(backlog.getStories().stream().filter(s -> s.getCreationDate().isAfter(startDate) && s.getCreationDate().isBefore(endDate)).collect(Collectors.toSet()));
        backlog.setTasks(backlog.getTasks().stream().filter(t -> t.getCreationDate().isAfter(startDate) && t.getCreationDate().isBefore(endDate)).collect(Collectors.toSet()));

        return backlog;
    }

    @Override
    public Backlog getWithIssuesByName(int id, String name) {
        Backlog backlog = backlogRepo.get(id);
        backlog.setBugs(backlog.getBugs().stream().filter(b -> b.getName().equals(name)).collect(Collectors.toSet()));
        backlog.setEpics(backlog.getEpics().stream().filter(e -> e.getName().equals(name)).collect(Collectors.toSet()));
        backlog.setStories(backlog.getStories().stream().filter(s -> s.getName().equals(name)).collect(Collectors.toSet()));
        backlog.setTasks(backlog.getTasks().stream().filter(t -> t.getName().equals(name)).collect(Collectors.toSet()));

        return backlog;
    }

    @Override
    public Backlog getWithIssuesByExecutor(int id, int executor_id) {
        Backlog backlog = backlogRepo.get(id);
        backlog.setBugs(backlog.getBugs().stream().filter(b -> b.getExecutor().getId() == executor_id).collect(Collectors.toSet()));
        backlog.setEpics(backlog.getEpics().stream().filter(e -> e.getExecutor().getId() == executor_id).collect(Collectors.toSet()));
        backlog.setStories(backlog.getStories().stream().filter(s -> s.getExecutor().getId() == executor_id).collect(Collectors.toSet()));
        backlog.setTasks(backlog.getTasks().stream().filter(t -> t.getExecutor().getId() == executor_id).collect(Collectors.toSet()));

        return backlog;
    }

    @Override
    public Backlog getWithIssuesByReporter(int id, int reporter_id) {
        Backlog backlog = backlogRepo.get(id);
        backlog.setBugs(backlog.getBugs().stream().filter(b -> b.getReporter().getId() == reporter_id).collect(Collectors.toSet()));
        backlog.setEpics(backlog.getEpics().stream().filter(e -> e.getReporter().getId() == reporter_id).collect(Collectors.toSet()));
        backlog.setStories(backlog.getStories().stream().filter(s -> s.getReporter().getId() == reporter_id).collect(Collectors.toSet()));
        backlog.setTasks(backlog.getTasks().stream().filter(t -> t.getReporter().getId() == reporter_id).collect(Collectors.toSet()));

        return backlog;
    }
}
