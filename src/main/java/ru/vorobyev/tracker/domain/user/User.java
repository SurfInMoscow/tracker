package ru.vorobyev.tracker.domain.user;

import lombok.*;
import ru.vorobyev.tracker.domain.AbstractBaseEntity;

@NoArgsConstructor
@Getter
@Setter
@ToString
public class User extends AbstractBaseEntity {
    private String name;

    private String email;

    private String password;

    public User(String name, String email, String password) {
        this(null, name, email, password);
    }

    public User(Integer id, String name, String email, String password) {
        super(id);
        this.name = name;
        this.email = email;
        this.password = password;
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
