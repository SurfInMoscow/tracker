package ru.vorobyev.tracker.repository.jpa.issue;

import ru.vorobyev.tracker.domain.issue.Epic;
import ru.vorobyev.tracker.repository.IssueRepository;

import java.util.List;

public class EpicJpaRepositoryImpl implements IssueRepository<Epic> {
    @Override
    public Epic save(Epic epic) {
        return null;
    }

    @Override
    public boolean delete(int id) {
        return false;
    }

    @Override
    public Epic get(int id) {
        return null;
    }

    @Override
    public Epic getByName(String name) {
        return null;
    }

    @Override
    public List<Epic> getAll() {
        return null;
    }

    public void clear() {}
}
