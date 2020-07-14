package ru.vorobyev.tracker.repository.jdbc.user;

import ru.vorobyev.tracker.domain.user.User;
import ru.vorobyev.tracker.repository.UserRepository;
import ru.vorobyev.tracker.repository.jdbc.BaseSqlHelper;
import ru.vorobyev.tracker.repository.jdbc.ConnectionFactory;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.*;
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
        if (user.isNew()) {
            return new BaseSqlHelper<User>(connectionFactory) {
                @Override
                public void processing(Connection connection) throws SQLException {
                    try (PreparedStatement ps = connection.prepareStatement("INSERT INTO users(email, name, password) VALUES (?, ?, ?)")) {
                        ps.setString(1, user.getEmail());
                        ps.setString(2, user.getName());
                        ps.setString(3, user.getPassword());
                        ps.execute();

                        try (ResultSet generatedKeys = ps.getGeneratedKeys()) {
                            if (generatedKeys.next()) {
                                user.setId(generatedKeys.getInt(1));
                            } else {
                                throw new SQLException("No ID generated.");
                            }
                        }
                    }

                    insertRoles(connection, user);
                    insertProjectRelation(connection, user);

                    setEntity(user);
                }
            }.getEntity();
        } else {
            return new BaseSqlHelper<User>(connectionFactory) {
                @Override
                public void processing(Connection connection) throws SQLException {

                }
            }.getEntity();
        }
        /*if (user.isNew()) {
            user.setId(SEQ_GENERATOR.getAndIncrement());
            userRepo.put(user.getId(), user);
            return user;
        }

        return userRepo.computeIfPresent(user.getId(), (key, value) -> value = user);*/
    }

    private void insertProjectRelation(Connection connection, User user) throws SQLException {
        try (PreparedStatement ps = connection.prepareStatement("INSERT INTO project_users(project_id, user_id) VALUES (?, ?)")) {
            user.getProjects().forEach(project -> {
                try {
                    ps.setInt(1, project.getId());
                    ps.setInt(2, user.getId());
                    ps.addBatch();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            });

            ps.executeBatch();
        }
    }

    private void insertRoles(Connection connection, User user) throws SQLException {
        try (PreparedStatement ps = connection.prepareStatement("INSERT INTO user_roles(user_id, roles) VALUES (?, ?)")) {
            user.getRoles().forEach(role -> {
                try {
                    ps.setInt(1, user.getId());
                    ps.setString(2, role.name());
                    ps.addBatch();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            });

            ps.executeBatch();
        }
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

    public void clear() {
        new BaseSqlHelper<User>(connectionFactory) {
            @Override
            public void processing(Connection connection) throws SQLException {
                try (PreparedStatement ps = connection.prepareStatement("DELETE FROM users")) {
                    ps.execute();
                }
            }
        };
    }
}
