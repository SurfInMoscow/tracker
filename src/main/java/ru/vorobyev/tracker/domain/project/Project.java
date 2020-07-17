package ru.vorobyev.tracker.domain.project;

import lombok.*;
import ru.vorobyev.tracker.domain.AbstractBaseEntity;
import ru.vorobyev.tracker.domain.user.User;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "projects")
@NoArgsConstructor
@Getter
@Setter
public class Project extends AbstractBaseEntity {

    @Column(name = "name")
    @NotNull
    @Size(min = 2, max = 100)
    private String name;

    @Column(name = "description")
    @NotNull
    @Size(min = 2)
    private String description;

    @Column(name = "department")
    @NotNull
    @Size(min = 2)
    private String department;

    @Column(name = "manager")
    @NotNull
    private String manager;

    @Column(name = "administrator")
    @NotNull
    private String administrator;

    @OneToOne(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.REMOVE})
    @JoinColumn(name = "backlog_id")
    @ToString.Exclude
    private Backlog backlog;

    @OneToOne(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.REMOVE})
    @JoinColumn(name = "sprint_id")
    @ToString.Exclude
    private Sprint sprint;

    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.MERGE)
    @JoinTable(
            name = "project_users",
            joinColumns = @JoinColumn(name = "project_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id")
    )
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
        this.participants = new HashSet<>();
    }
}
