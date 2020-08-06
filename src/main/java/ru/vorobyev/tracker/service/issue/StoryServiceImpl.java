package ru.vorobyev.tracker.service.issue;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.vorobyev.tracker.domain.issue.Story;
import ru.vorobyev.tracker.repository.IssueRepository;
import ru.vorobyev.tracker.service.IssueService;

import java.util.List;

@Service
@Slf4j
public class StoryServiceImpl implements IssueService<Story> {

    private IssueRepository<Story> issueRepository;

    @Autowired
    public StoryServiceImpl(@Qualifier("StoryRepository") IssueRepository<Story> issueRepository) {
        this.issueRepository = issueRepository;
    }

    @Override
    public Story save(Story story) {
        log.info(story.toString() + " created/updated.");
        return issueRepository.save(story);
    }

    @Override
    public boolean delete(int id) {
        log.info(String.format("Story with id:%d deleted.", id));
        return issueRepository.delete(id);
    }

    @Override
    public Story get(int id) {
        log.info(String.format("Get story with id:%d.", id));
        return issueRepository.get(id);
    }

    @Override
    public Story getByName(String name) {
        log.info(String.format("Get story with name:%s.", name));
        return issueRepository.getByName(name);
    }

    @Override
    public List<Story> getAll() {
        log.info("Get all stories action.");
        return issueRepository.getAll();
    }
}
