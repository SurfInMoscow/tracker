package ru.vorobyev.tracker.repository.inmemory.issue;

import ru.vorobyev.tracker.domain.issue.Story;

import static ru.vorobyev.tracker.repository.inmemory.SeqGenerator.SEQ_GENERATOR;

public class StoryRepositoryImpl extends AbstractIssueRepositoryImpl<Story> {
    @Override
    public Story save(Story story) {
        if (story.isNew()) {
            story.setId(SEQ_GENERATOR.incrementAndGet());
            issueRepo.put(story.getId(), story);
            return story;
        }

        return issueRepo.computeIfPresent(story.getId(), (key, value) -> value = story);
    }

    @Override
    public Story getByName(String name) {
        return issueRepo.values().stream().filter(story -> story.getName().equals(name)).findFirst().get();
    }
}
