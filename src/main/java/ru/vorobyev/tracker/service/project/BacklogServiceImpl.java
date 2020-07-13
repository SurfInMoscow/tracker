package ru.vorobyev.tracker.service.project;

import ru.vorobyev.tracker.domain.project.Backlog;
import ru.vorobyev.tracker.repository.BacklogRepository;
import ru.vorobyev.tracker.service.BacklogService;

import java.util.List;

public class BacklogServiceImpl implements BacklogService {

    private BacklogRepository backlogRepository;

    public BacklogServiceImpl(BacklogRepository backlogRepository) {
        this.backlogRepository = backlogRepository;
    }

    @Override
    public Backlog save(Backlog backlog) {
        return backlogRepository.save(backlog);
    }

    @Override
    public boolean delete(int id) {
        return backlogRepository.delete(id);
    }

    @Override
    public Backlog get(int id) {
        return backlogRepository.get(id);
    }

    @Override
    public List<Backlog> getAll() {
        return backlogRepository.getAll();
    }
}
