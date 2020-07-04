package ru.vorobyev.tracker.domain.project;

import ru.vorobyev.tracker.domain.issue.Bug;
import ru.vorobyev.tracker.domain.issue.Epic;
import ru.vorobyev.tracker.domain.issue.Story;
import ru.vorobyev.tracker.domain.issue.Task;

import java.util.Set;

public interface ProjectIssues {
    Set<Bug> getBugs();

    void setBugs(Set<Bug> bugs);

    Set<Epic> getEpics();

    void setEpics(Set<Epic> epics);

    Set<Story> getStories();

    void setStories(Set<Story> stories);

    Set<Task> getTasks();

    void setTasks(Set<Task> tasks);
}
