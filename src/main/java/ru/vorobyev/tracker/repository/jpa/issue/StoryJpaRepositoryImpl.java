package ru.vorobyev.tracker.repository.jpa.issue;

import ru.vorobyev.tracker.domain.issue.Story;
import ru.vorobyev.tracker.repository.IssueRepository;

import java.util.List;

public class StoryJpaRepositoryImpl implements IssueRepository<Story> {
    @Override
    public Story save(Story issue) {
        return null;
    }

    @Override
    public boolean delete(int id) {
        return false;
    }

    @Override
    public Story get(int id) {
        return null;
    }

    @Override
    public Story getByName(String name) {
        return null;
    }

    @Override
    public List<Story> getAll() {
        return null;
    }

    public void clear() {}
}
