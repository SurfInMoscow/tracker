package ru.vorobyev.tracker.domain.user;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import ru.vorobyev.tracker.domain.AbstractBaseEntity;
import ru.vorobyev.tracker.domain.project.Project;

import java.util.EnumSet;
import java.util.Set;

@NoArgsConstructor
@Getter
@Setter
@ToString
public class User extends AbstractBaseEntity {
    private String name;

    private String email;

    private String password;

    @ToString.Exclude
    private Set<Project> projects;

    private Set<Role> roles;

    public User(String name, String email, String password, Set<Project> projects, Role role, Role... roles) {
        this(null, name, email, password, projects, role, roles);
    }

    public User(Integer id, String name, String email, String password, Set<Project> projects, Role role, Role... roles) {
        super(id);
        this.name = name;
        this.email = email;
        this.password = password;
        this.projects = projects;
        this.roles = EnumSet.of(role, roles);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        User user = (User) o;

        if (!name.equals(user.name)) return false;
        return email.equals(user.email);
    }

    @Override
    public int hashCode() {
        int result = name.hashCode();
        result = 31 * result + email.hashCode();
        return result;
    }
}
