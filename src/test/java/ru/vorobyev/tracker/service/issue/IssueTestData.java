package ru.vorobyev.tracker.service.issue;

import ru.vorobyev.tracker.domain.issue.*;
import ru.vorobyev.tracker.domain.issue.workflow.WorkflowStatus;

import java.time.LocalDateTime;

public class IssueTestData {
    public static final Bug BUG1 = new Bug(Priority.LOW, LocalDateTime.now(), "First bug", null, null, WorkflowStatus.OPEN_ISSUE);
    public static final Bug BUG2 = new Bug(Priority.MEDIUM, LocalDateTime.now(), "Second bug", null, null, WorkflowStatus.IN_PROGRESS_ISSUE);
    public static final Bug BUG3 = new Bug(Priority.HIGH, LocalDateTime.now(), "Third bug", null, null, WorkflowStatus.RESOLVED_ISSUE);

    public static final Epic EPIC1 = new Epic(Priority.LOW, LocalDateTime.now(), "First epic", null, null, WorkflowStatus.TEST_ISSUE);
    public static final Epic EPIC2 = new Epic(Priority.LOW, LocalDateTime.now(), "Second epic", null, null, WorkflowStatus.TEST_ISSUE);
    public static final Epic EPIC3 = new Epic(Priority.LOW, LocalDateTime.now(), "Third epic", null, null, WorkflowStatus.TEST_ISSUE);

    public static final Story STORY1 = new Story(Priority.LOW, LocalDateTime.now(), "First story", null, null, WorkflowStatus.TEST_ISSUE);
    public static final Story STORY2 = new Story(Priority.LOW, LocalDateTime.now(), "Second story", null, null, WorkflowStatus.IN_PROGRESS_ISSUE);
    public static final Story STORY3 = new Story(Priority.LOW, LocalDateTime.now(), "Third story", null, null, WorkflowStatus.CLOSE_ISSUE);

    public static final Task TASK1 = new Task(Priority.LOW, LocalDateTime.now(), "First task", null, null, WorkflowStatus.TEST_ISSUE);
    public static final Task TASK2 = new Task(Priority.LOW, LocalDateTime.now(), "Second task", null, null, WorkflowStatus.OPEN_ISSUE);
    public static final Task TASK3 = new Task(Priority.LOW, LocalDateTime.now(), "Third task", null, null, WorkflowStatus.RE_OPENED_ISSUE);
}
