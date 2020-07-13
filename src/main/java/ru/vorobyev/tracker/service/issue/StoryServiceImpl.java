package ru.vorobyev.tracker.service.issue;

import ru.vorobyev.tracker.domain.issue.Story;
import ru.vorobyev.tracker.repository.IssueRepository;
import ru.vorobyev.tracker.service.IssueService;

import java.util.List;

public class StoryServiceImpl implements IssueService<Story> {

    private IssueRepository<Story> issueRepository;

    public StoryServiceImpl(IssueRepository<Story> issueRepository) {
        this.issueRepository = issueRepository;
    }

    @Override
    public Story save(Story story) {
        return issueRepository.save(story);
    }

    @Override
    public boolean delete(int id) {
        return issueRepository.delete(id);
    }

    @Override
    public Story get(int id) {
        return issueRepository.get(id);
    }

    @Override
    public Story getByName(String name) {
        return issueRepository.getByName(name);
    }

    @Override
    public List<Story> getAll() {
        return issueRepository.getAll();
    }
}
