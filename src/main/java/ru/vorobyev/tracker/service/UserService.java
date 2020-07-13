package ru.vorobyev.tracker.service;

import ru.vorobyev.tracker.domain.user.User;

import java.util.List;

public interface UserService {
    User save(User user);

    boolean delete(int id);

    User get(int id);

    User getByEmail(String email);

    List<User> getAll();
}
