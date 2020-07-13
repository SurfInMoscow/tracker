package ru.vorobyev.tracker.service.project;

import ru.vorobyev.tracker.domain.project.Sprint;
import ru.vorobyev.tracker.repository.SprintRepository;
import ru.vorobyev.tracker.service.SprintService;

import java.util.List;

public class SprintServiceImpl implements SprintService {

    private SprintRepository sprintRepository;

    public SprintServiceImpl(SprintRepository sprintRepository) {
        this.sprintRepository = sprintRepository;
    }

    @Override
    public Sprint save(Sprint sprint) {
        return sprintRepository.save(sprint);
    }

    @Override
    public boolean delete(int id) {
        return sprintRepository.delete(id);
    }

    @Override
    public Sprint get(int id) {
        return sprintRepository.get(id);
    }

    @Override
    public List<Sprint> getAll() {
        return sprintRepository.getAll();
    }
}
