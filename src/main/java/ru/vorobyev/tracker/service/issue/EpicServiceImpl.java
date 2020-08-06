package ru.vorobyev.tracker.service.issue;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.vorobyev.tracker.domain.issue.Epic;
import ru.vorobyev.tracker.repository.IssueRepository;
import ru.vorobyev.tracker.service.IssueService;

import java.util.List;

@Service
@Slf4j
public class EpicServiceImpl implements IssueService<Epic> {

    private IssueRepository<Epic> issueRepository;

    @Autowired
    public EpicServiceImpl(@Qualifier("EpicRepository") IssueRepository<Epic> issueRepository) {
        this.issueRepository = issueRepository;
    }

    @Override
    public Epic save(Epic epic) {
        log.info(epic.toString() + " created/updated.");
        return issueRepository.save(epic);
    }

    @Override
    public boolean delete(int id) {
        log.info(String.format("Epic with id:%d deleted.", id));
        return issueRepository.delete(id);
    }

    @Override
    public Epic get(int id) {
        log.info(String.format("Get epic with id:%d.", id));
        return issueRepository.get(id);
    }

    @Override
    public Epic getByName(String name) {
        log.info(String.format("Get epic with name:%s.", name));
        return issueRepository.getByName(name);
    }

    @Override
    public List<Epic> getAll() {
        log.info("Get all epics action.");
        return issueRepository.getAll();
    }
}
