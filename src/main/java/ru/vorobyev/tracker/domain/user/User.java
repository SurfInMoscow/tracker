package ru.vorobyev.tracker.domain.user;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.BatchSize;
import org.hibernate.annotations.Fetch;
import ru.vorobyev.tracker.domain.AbstractBaseEntity;
import ru.vorobyev.tracker.domain.project.Project;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.EnumSet;
import java.util.Set;

@NamedQueries({
        @NamedQuery(name = User.DELETE, query = "DELETE FROM User u WHERE u.id=:id"),
        @NamedQuery(name = User.BY_EMAIL, query = "SELECT DISTINCT u FROM User u LEFT JOIN FETCH u.roles WHERE u.email=:email"),
        @NamedQuery(name = User.GET_ALL, query = "SELECT u FROM User u ORDER BY u.name, u.email")
})
@Entity
@Table(name = "users")
@NoArgsConstructor
@Getter
@Setter
@ToString
public class User extends AbstractBaseEntity {

    public static final String DELETE = "user.delete";
    public static final String BY_EMAIL = "user.getByEmail";
    public static final String GET_ALL = "user.getAll";

    @Column
    @NotNull
    @Size(min = 2, max = 50)
    private String name;

    @Column(name = "email", unique = true)
    @NotNull
    @Email
    @Size(max = 20)
    private String email;

    @Column(name = "password")
    @NotNull
    @Size(min = 5, max = 100)
    @ToString.Exclude
    private String password;

    @ManyToMany(mappedBy = "participants", fetch = FetchType.LAZY)
    @ToString.Exclude
    private Set<Project> projects;

    @Column(name = "roles")
    @Enumerated(EnumType.STRING)
    @CollectionTable(name = "user_roles", joinColumns = @JoinColumn(name = "user_id"))
    @ElementCollection(fetch = FetchType.EAGER)
    @BatchSize(size = 200)
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
