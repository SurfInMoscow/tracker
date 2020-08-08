package ru.vorobyev.tracker.to.project;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.vorobyev.tracker.domain.project.Project;
import ru.vorobyev.tracker.domain.user.User;

import java.util.Set;

@NoArgsConstructor
@Getter
@Setter
public class ProjectTo {

    private Integer id;

    private String name;

    private String description;

    private String department;

    private String manager;

    private String administrator;

    private Integer backlog_id;

    private Integer sprint_id;

    @JsonIgnoreProperties({"hibernateLazyInitializer"})
    private Set<User> participants;

    public ProjectTo(Project project) {
        if (!project.isNew()) {
            this.id = project.getId();
        }
        populate(project);
    }

    private void populate(Project project) {
        this.name = project.getName();
        this.description = project.getDescription();
        this.department = project.getDepartment();
        this.manager = project.getManager();
        this.administrator = project.getAdministrator();
        this.backlog_id = project.getBacklog() == null ? null : project.getBacklog().getId();
        this.sprint_id = project.getSprint() == null ? null : project.getSprint().getId();
        this.participants = project.getParticipants();
    }
}
