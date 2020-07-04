package ru.vorobyev.tracker.domain.issue.workflow;

public enum WorkflowStatus {
    OPEN_ISSUE("Open Issue"),
    IN_PROGRESS_ISSUE("InProgress Issue"),
    REVIEW_ISSUE("Review Issue"),
    TEST_ISSUE("Test Issue"),
    RESOLVED_ISSUE("Resolved Issue"),
    RE_OPENED_ISSUE("ReOpened Issue"),
    CLOSE_ISSUE("Close Issue");

    private String text;

    WorkflowStatus(String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }
}
