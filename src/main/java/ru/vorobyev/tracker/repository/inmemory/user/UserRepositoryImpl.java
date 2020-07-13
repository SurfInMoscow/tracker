package ru.vorobyev.tracker.repository.inmemory.user;

import lombok.Getter;
import ru.vorobyev.tracker.domain.user.User;
import ru.vorobyev.tracker.repository.UserRepository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static ru.vorobyev.tracker.repository.inmemory.SeqGenerator.SEQ_GENERATOR;

@Getter
public class UserRepositoryImpl implements UserRepository {

    private Map<Integer, User> userRepo;

    public UserRepositoryImpl() {
        userRepo = new HashMap<>();
    }

    @Override
    public User save(User user) {
        if (user.isNew()) {
            user.setId(SEQ_GENERATOR.getAndIncrement());
            userRepo.put(user.getId(), user);
            return user;
        }

        return userRepo.computeIfPresent(user.getId(), (key, value) -> value = user);
    }

    @Override
    public boolean delete(int id) {
        return userRepo.remove(id) != null;
    }

    @Override
    public User get(int id) {
        return userRepo.get(id);
    }

    @Override
    public User getByEmail(String email) {
        return userRepo.values().stream().filter(user -> user.getEmail().equals(email)).findFirst().get();
    }

    @Override
    public List<User> getAll() {
        return new ArrayList<>(userRepo.values());
    }
}
