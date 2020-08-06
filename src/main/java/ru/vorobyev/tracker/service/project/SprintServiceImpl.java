package ru.vorobyev.tracker.service.project;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.vorobyev.tracker.domain.project.Sprint;
import ru.vorobyev.tracker.repository.SprintRepository;
import ru.vorobyev.tracker.service.SprintService;

import java.util.List;

@Service
@Slf4j
public class SprintServiceImpl implements SprintService {

    private SprintRepository sprintRepository;

    @Autowired
    public SprintServiceImpl(SprintRepository sprintRepository) {
        this.sprintRepository = sprintRepository;
    }

    @Override
    public Sprint save(Sprint sprint) {
        log.info(sprint.toString() + " created.");
        return sprintRepository.save(sprint);
    }

    @Override
    public boolean delete(int id) {
        log.info(String.format("Sprint with id:%d deleted.", id));
        return sprintRepository.delete(id);
    }

    @Override
    public Sprint get(int id) {
        log.info(String.format("Get Sprint with id:%d.", id));
        return sprintRepository.get(id);
    }

    @Override
    public List<Sprint> getAll() {
        log.info("Get all Sprints action");
        return sprintRepository.getAll();
    }
}
