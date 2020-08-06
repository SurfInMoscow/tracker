package ru.vorobyev.tracker.service.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.vorobyev.tracker.domain.user.User;
import ru.vorobyev.tracker.repository.UserRepository;
import ru.vorobyev.tracker.service.UserService;

import java.util.List;

@Service
@Slf4j
public class UserServiceImpl implements UserService {

    private UserRepository userRepository;

    private PasswordEncoder passwordEncoder;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public User save(User user) {
        log.info(user.toString() + " saved.");
        return userRepository.save(prepareAndSave(user));
    }

    @Override
    public boolean delete(int id) {
        log.info(String.format("User with id:%d deleted.", id));
        return userRepository.delete(id);
    }

    @Override
    public User get(int id) {
        log.info(String.format("Get user by id:%d.", id));
        return userRepository.get(id);
    }

    @Override
    public User getByEmail(String email) {
        log.info(String.format("Get user by email:%s.", email));
        return userRepository.getByEmail(email);
    }

    @Override
    public List<User> getAll() {
        log.info("Get all users action.");
        return userRepository.getAll();
    }

    private User prepareAndSave(User user) {
       String password = user.getPassword();
       user.setPassword(passwordEncoder.encode(password));
       user.setEmail(user.getEmail().toLowerCase());

       return user;
    }
}
