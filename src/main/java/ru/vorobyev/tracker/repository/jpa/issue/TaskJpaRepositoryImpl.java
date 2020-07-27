package ru.vorobyev.tracker.repository.jpa.issue;

import ru.vorobyev.tracker.domain.issue.Task;
import ru.vorobyev.tracker.repository.IssueRepository;

import java.util.List;

public class TaskJpaRepositoryImpl implements IssueRepository<Task> {
    @Override
    public Task save(Task issue) {
        return null;
    }

    @Override
    public boolean delete(int id) {
        return false;
    }

    @Override
    public Task get(int id) {
        return null;
    }

    @Override
    public Task getByName(String name) {
        return null;
    }

    @Override
    public List<Task> getAll() {
        return null;
    }

    public void clear() {}
}
