package ru.vorobyev.tracker.domain.issue;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.vorobyev.tracker.domain.issue.workflow.WorkflowStatus;
import ru.vorobyev.tracker.domain.user.User;

import java.time.LocalDateTime;

@NoArgsConstructor
@Getter
@Setter
public class Story extends AbstractIssue {

    private Bug rootBug;

    private Epic rootEpic;

    private Task rootTask;

    public Story(Issue issue) {
        super(issue);
    }

    public Story(Priority priority, LocalDateTime creationDate, String name, User executor, User reporter, WorkflowStatus status) {
        super(priority, creationDate, name, executor, reporter, status);
    }
}
