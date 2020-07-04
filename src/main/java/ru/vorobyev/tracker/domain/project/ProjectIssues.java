package ru.vorobyev.tracker.domain.project;

import ru.vorobyev.tracker.domain.issue.Issue;

import java.util.List;

public interface ProjectIssues {
    List<Issue> getIssues();

    void setIssues(List<Issue> issues);
}
