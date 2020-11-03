package ru.vorobyev.tracker.domain.user;

import org.springframework.security.core.GrantedAuthority;

public enum Role implements GrantedAuthority {
    ROLE_USER,
    ROLE_ADMIN,
    ROLE_SYSTEM;

    @Override
    public String getAuthority() {
        return name();
    }
}
