package ru.vorobyev.tracker.domain.issue;

import lombok.AllArgsConstructor;
import ru.vorobyev.tracker.domain.issue.workflow.WorkflowStatus;
import ru.vorobyev.tracker.domain.user.User;

import java.time.LocalDateTime;

@AllArgsConstructor
public class Epic extends AbstractIssue {
    public Epic(Priority priority, LocalDateTime creationDate, String name, User executor, User reporter, WorkflowStatus status) {
        super(priority, creationDate, name, executor, reporter, status);
    }
}
