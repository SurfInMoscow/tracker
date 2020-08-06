package ru.vorobyev.tracker.service.issue;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.vorobyev.tracker.domain.issue.Bug;
import ru.vorobyev.tracker.repository.IssueRepository;
import ru.vorobyev.tracker.service.IssueService;

import java.util.List;

@Service
@Slf4j
public class BugServiceImpl implements IssueService<Bug> {

    private final IssueRepository<Bug> issueRepository;

    @Autowired
    public BugServiceImpl(@Qualifier("BugRepository") IssueRepository<Bug> issueRepository) {
        this.issueRepository = issueRepository;
    }

    @Override
    public Bug save(Bug bug) {
        log.info(bug.toString() + " created/updated.");
        return issueRepository.save(bug);
    }

    @Override
    public boolean delete(int id) {
        log.info(String.format("Bug with id:%d deleted.", id));
        return issueRepository.delete(id);
    }

    @Override
    public Bug get(int id) {
        log.info(String.format("Get bug with id:%d.", id));
        return issueRepository.get(id);
    }

    @Override
    public Bug getByName(String name) {
        log.info(String.format("Get bug with name:%s.", name));
        return issueRepository.getByName(name);
    }

    @Override
    public List<Bug> getAll() {
        log.info("Get all bugs action.");
        return issueRepository.getAll();
    }
}
