package ru.vorobyev.tracker.repository.jdbc.user;

import ru.vorobyev.tracker.domain.user.User;
import ru.vorobyev.tracker.repository.UserRepository;
import ru.vorobyev.tracker.repository.jdbc.ConnectionFactory;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.DriverManager;
import java.util.List;
import java.util.Properties;

public class UserJdbcRepositoryImpl implements UserRepository {

    private final ConnectionFactory connectionFactory;

    public UserJdbcRepositoryImpl() {
        Properties properties = new Properties();
        try {
            properties.load(new FileInputStream(this.getClass().getClassLoader().getResource("jdbc/db.properties").getFile()));
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            Class.forName(properties.getProperty("db.className"));
        } catch (ClassNotFoundException e) {
            throw new IllegalStateException(e);
        }
        String url = properties.getProperty("db.url");
        String user = properties.getProperty("db.user");
        String password = properties.getProperty("db.password");
        this.connectionFactory = () -> DriverManager.getConnection(url, user, password);
    }

    @Override
    public User save(User user) {
        /*if (user.isNew()) {
            user.setId(SEQ_GENERATOR.getAndIncrement());
            userRepo.put(user.getId(), user);
            return user;
        }

        return userRepo.computeIfPresent(user.getId(), (key, value) -> value = user);*/
        return null;
    }

    @Override
    public boolean delete(int id) {
        /*return userRepo.remove(id) != null;*/
        return true;
    }

    @Override
    public User get(int id) {
        /*return userRepo.get(id);*/
        return null;
    }

    @Override
    public User getByEmail(String email) {
        /*return userRepo.values().stream().filter(user -> user.getEmail().equals(email)).findFirst().get();*/
        return null;
    }

    @Override
    public List<User> getAll() {
        /*return new ArrayList<>(userRepo.values());*/
        return null;
    }
}
