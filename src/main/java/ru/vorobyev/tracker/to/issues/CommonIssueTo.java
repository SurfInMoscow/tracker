package ru.vorobyev.tracker.to.issues;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.vorobyev.tracker.domain.issue.Priority;
import ru.vorobyev.tracker.domain.issue.workflow.WorkflowStatus;

import java.time.LocalDateTime;

@NoArgsConstructor
@Getter
@Setter
public class CommonIssueTo {

    private Priority priority;

    private LocalDateTime creationDate;

    private String name;

    private Integer executor_id;

    private Integer reporter_id;

    private WorkflowStatus status;

    private Integer backlog_id;

    private Integer sprint_id;
}
