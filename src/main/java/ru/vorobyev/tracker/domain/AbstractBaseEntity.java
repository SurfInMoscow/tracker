package ru.vorobyev.tracker.domain;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class AbstractBaseEntity {
    protected Integer id;

    public boolean isNew() {
        return this.id == null;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        AbstractBaseEntity that = (AbstractBaseEntity) o;

        return id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }
}
