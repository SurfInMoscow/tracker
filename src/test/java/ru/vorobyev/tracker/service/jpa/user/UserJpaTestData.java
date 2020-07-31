package ru.vorobyev.tracker.service.jpa.user;

import ru.vorobyev.tracker.domain.user.User;

import java.util.HashSet;

import static ru.vorobyev.tracker.domain.user.Role.ROLE_ADMIN;
import static ru.vorobyev.tracker.domain.user.Role.ROLE_USER;

public class UserJpaTestData {
    public static final User USER1 = new User("Petya", "abc@yahoo.com", "password", new HashSet<>(), ROLE_USER);
    public static final User USER2 = new User("Katya", "qwerty@sberbank.ru", "katkapass", new HashSet<>(), ROLE_USER, ROLE_ADMIN);
    public static final User USER3 = new User("Vasya", "kmkm@ya.ru", "etovaska", new HashSet<>(), ROLE_USER);
}
