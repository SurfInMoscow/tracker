package ru.vorobyev.tracker.domain.project;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.vorobyev.tracker.domain.AbstractBaseEntity;
import ru.vorobyev.tracker.domain.issue.Issue;

import java.util.List;

@NoArgsConstructor
@Getter
@Setter
public class Sprint extends AbstractBaseEntity implements ProjectIssues {
    private List<Issue> issues;

    public Sprint(List<Issue> issues) {
        this(null, issues);
    }

    public Sprint(Integer id, List<Issue> issues) {
        super(id);
        this.issues = issues;
    }
}
