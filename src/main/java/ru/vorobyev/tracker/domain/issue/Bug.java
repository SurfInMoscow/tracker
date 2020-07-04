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
public class Bug extends AbstractIssue {

    private Epic rootEpic;

    private Story rootStory;

    private Task rootTask;

    public Bug(Issue issue) {
        super(issue);
    }

    public Bug(Priority priority, LocalDateTime creationDate, String name, User executor, User reporter, WorkflowStatus status) {
        super(priority, creationDate, name, executor, reporter, status);
    }
}
