package ru.vorobyev.tracker.repository.inmemory.issue;

import ru.vorobyev.tracker.domain.issue.Epic;

import static ru.vorobyev.tracker.repository.inmemory.SeqGenerator.EPIC_SEQ_GENERATOR;

public class EpicRepositoryImpl extends AbstractIssueRepositoryImpl<Epic> {
    @Override
    public Epic save(Epic epic) {
        if (epic.isNew()) {
            epic.setId(EPIC_SEQ_GENERATOR.incrementAndGet());
            issueRepo.put(epic.getId(), epic);
            return epic;
        }

        return issueRepo.computeIfPresent(epic.getId(), (key, value) -> value = epic);
    }

    @Override
    public Epic getByName(String name) {
        return issueRepo.values().stream().filter(epic -> epic.getName().equals(name)).findFirst().get();
    }
}
