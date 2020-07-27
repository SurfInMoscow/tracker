package ru.vorobyev.tracker.repository.jpa.user;

import ru.vorobyev.tracker.domain.user.User;
import ru.vorobyev.tracker.repository.UserRepository;

import java.util.List;

public class UserJpaRepositoryImpl implements UserRepository {
    @Override
    public User save(User user) {
        return null;
    }

    @Override
    public boolean delete(int id) {
        return false;
    }

    @Override
    public User get(int id) {
        return null;
    }

    @Override
    public User getByEmail(String email) {
        return null;
    }

    @Override
    public List<User> getAll() {
        return null;
    }

    public void clear() {
    }
}
