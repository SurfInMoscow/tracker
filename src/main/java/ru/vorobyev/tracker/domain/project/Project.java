package ru.vorobyev.tracker.domain.project;

import lombok.*;
import ru.vorobyev.tracker.domain.AbstractBaseEntity;
import ru.vorobyev.tracker.domain.user.User;

import java.util.Set;

@NoArgsConstructor
@Getter
@Setter
public class Project extends AbstractBaseEntity {
    private String name;

    private String description;

    private String department;

    private String manager;

    private String administrator;

    @ToString.Exclude
    private Backlog backlog;

    @ToString.Exclude
    private Sprint sprint;

    @ToString.Exclude
    private Set<User> participants;

    public Project(String name, String description, String department, String manager, String administrator) {
        this(null, name, description, department, manager, administrator, new Backlog(), new Sprint());
    }

    public Project(Integer id, String name, String description, String department, String manager, String administrator, Backlog backlog, Sprint sprint) {
        super(id);
        this.name = name;
        this.description = description;
        this.department = department;
        this.manager = manager;
        this.administrator = administrator;
        this.backlog = backlog;
        this.sprint = sprint;
    }
}
