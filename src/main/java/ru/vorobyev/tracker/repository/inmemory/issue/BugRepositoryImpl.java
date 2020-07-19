package ru.vorobyev.tracker.repository.inmemory.issue;

import ru.vorobyev.tracker.domain.issue.Bug;

import static ru.vorobyev.tracker.repository.inmemory.SeqGenerator.BUG_SEQ_GENERATOR;

public class BugRepositoryImpl extends AbstractIssueRepositoryImpl<Bug> {
    @Override
    public Bug save(Bug bug) {
        if (bug.isNew()) {
            bug.setId(BUG_SEQ_GENERATOR.incrementAndGet());
            issueRepo.put(bug.getId(), bug);
            return bug;
        }

        return issueRepo.computeIfPresent(bug.getId(), (key, value) -> value = bug);
    }

    @Override
    public Bug getByName(String name) {
        return issueRepo.values().stream().filter(bug -> bug.getName().equals(name)).findFirst().get();
    }
}
