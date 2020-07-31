package ru.vorobyev.tracker.service.issue;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.vorobyev.tracker.domain.issue.Task;
import ru.vorobyev.tracker.repository.IssueRepository;
import ru.vorobyev.tracker.service.IssueService;

import java.util.List;

@Service
public class TaskServiceImpl implements IssueService<Task> {

    private IssueRepository<Task> issueRepository;

    @Autowired
    public TaskServiceImpl(@Qualifier("TaskRepository") IssueRepository<Task> issueRepository) {
        this.issueRepository = issueRepository;
    }

    @Override
    public Task save(Task task) {
        return issueRepository.save(task);
    }

    @Override
    public boolean delete(int id) {
        return issueRepository.delete(id);
    }

    @Override
    public Task get(int id) {
        return issueRepository.get(id);
    }

    @Override
    public Task getByName(String name) {
        return issueRepository.getByName(name);
    }

    @Override
    public List<Task> getAll() {
        return issueRepository.getAll();
    }
}
