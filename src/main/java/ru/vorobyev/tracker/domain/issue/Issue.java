package ru.vorobyev.tracker.domain.issue;

import ru.vorobyev.tracker.domain.issue.workflow.WorkflowStatus;
import ru.vorobyev.tracker.domain.user.User;

import java.time.LocalDateTime;

public interface Issue {
    Priority getPriority();

    void setPriority(Priority priority);

    LocalDateTime getCreationDate();

    void setCreationDate(LocalDateTime creationDate);

    String getName();

    void setName(String name);

    User getExecutor();

    void setExecutor(User executor);

    User getReporter();

    void setReporter(User reporter);

    WorkflowStatus getStatus();

    void setStatus(WorkflowStatus status);
}
