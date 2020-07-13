package ru.vorobyev.tracker.repository.inmemory.issue;

import lombok.Getter;
import ru.vorobyev.tracker.repository.IssueRepository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Getter
public abstract class AbstractIssueRepositoryImpl<T> implements IssueRepository<T> {

    protected Map<Integer, T> issueRepo;

    public AbstractIssueRepositoryImpl() {
        issueRepo = new HashMap<>();
    }

    @Override
    public abstract T save(T issue);

    @Override
    public boolean delete(int id) {
        return issueRepo.remove(id) != null;
    }

    @Override
    public T get(int id) {
        return issueRepo.get(id);
    }

    @Override
    public abstract T getByName(String name);

    @Override
    public List<T> getAll() {
        return new ArrayList<>(issueRepo.values());
    }
}
