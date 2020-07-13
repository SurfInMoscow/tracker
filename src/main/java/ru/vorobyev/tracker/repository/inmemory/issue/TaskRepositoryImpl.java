package ru.vorobyev.tracker.repository.inmemory.issue;

import ru.vorobyev.tracker.domain.issue.Task;

import static ru.vorobyev.tracker.repository.inmemory.SeqGenerator.SEQ_GENERATOR;

public class TaskRepositoryImpl extends AbstractIssueRepositoryImpl<Task> {
    @Override
    public Task save(Task task) {
        if (task.isNew()) {
            task.setId(SEQ_GENERATOR.incrementAndGet());
            issueRepo.put(task.getId(), task);
            return task;
        }

        return issueRepo.computeIfPresent(task.getId(), (key, value) -> value = task);
    }

    @Override
    public Task getByName(String name) {
        return issueRepo.values().stream().filter(task -> task.getName().equals(name)).findFirst().get();
    }
}
