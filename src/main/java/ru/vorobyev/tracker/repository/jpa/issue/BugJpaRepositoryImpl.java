package ru.vorobyev.tracker.repository.jpa.issue;

import ru.vorobyev.tracker.domain.issue.Bug;
import ru.vorobyev.tracker.repository.IssueRepository;

import java.util.List;

public class BugJpaRepositoryImpl implements IssueRepository<Bug> {
    @Override
    public Bug save(Bug bug) {
        return null;
    }

    @Override
    public boolean delete(int id) {
        return false;
    }

    @Override
    public Bug get(int id) {
        return null;
    }

    @Override
    public Bug getByName(String name) {
        return null;
    }

    @Override
    public List<Bug> getAll() {
        return null;
    }

    public void clear() {}
}
