package ru.vorobyev.tracker.repository.jdbc;

import ru.vorobyev.tracker.domain.issue.Priority;
import ru.vorobyev.tracker.domain.issue.workflow.WorkflowStatus;

public class JdbcRepoUtils {
    public static Priority getPriority(String priority) {
        return  priority.equals("LOW") ?
                Priority.LOW : priority.equals("MEDIUM") ?
                Priority.MEDIUM : Priority.HIGH;
    }

    public static WorkflowStatus getWorkflowStatus(String status) {
        switch (status) {
            case "OPEN_ISSUE":
                return WorkflowStatus.OPEN_ISSUE;
            case "IN_PROGRESS_ISSUE":
                return WorkflowStatus.IN_PROGRESS_ISSUE;
            case "REVIEW_ISSUE":
                return WorkflowStatus.REVIEW_ISSUE;
            case "TEST_ISSUE":
                return WorkflowStatus.TEST_ISSUE;
            case "RESOLVED_ISSUE":
                return WorkflowStatus.RESOLVED_ISSUE;
            case "RE_OPENED_ISSUE":
                return WorkflowStatus.RE_OPENED_ISSUE;
            case "CLOSE_ISSUE":
                return WorkflowStatus.CLOSE_ISSUE;
        }

        return null;
    }
}
