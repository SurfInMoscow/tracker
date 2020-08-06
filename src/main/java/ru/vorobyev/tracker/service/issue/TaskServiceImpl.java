package ru.vorobyev.tracker.service.issue;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.vorobyev.tracker.domain.issue.Task;
import ru.vorobyev.tracker.repository.IssueRepository;
import ru.vorobyev.tracker.service.IssueService;

import java.util.List;

@Service
@Slf4j
public class TaskServiceImpl implements IssueService<Task> {

    private IssueRepository<Task> issueRepository;

    @Autowired
    public TaskServiceImpl(@Qualifier("TaskRepository") IssueRepository<Task> issueRepository) {
        this.issueRepository = issueRepository;
    }

    @Override
    public Task save(Task task) {
        log.info(task.toString() + " created/updated.");
        return issueRepository.save(task);
    }

    @Override
    public boolean delete(int id) {
        log.info(String.format("Task with id:%d deleted.", id));
        return issueRepository.delete(id);
    }

    @Override
    public Task get(int id) {
        log.info(String.format("Get task with id:%d.", id));
        return issueRepository.get(id);
    }

    @Override
    public Task getByName(String name) {
        log.info(String.format("Get task with name:%s.", name));
        return issueRepository.getByName(name);
    }

    @Override
    public List<Task> getAll() {
        log.info("Get all tasks action.");
        return issueRepository.getAll();
    }
}
