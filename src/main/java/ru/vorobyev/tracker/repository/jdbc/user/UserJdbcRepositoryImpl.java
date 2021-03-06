package ru.vorobyev.tracker.repository.jdbc.user;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;
import ru.vorobyev.tracker.domain.project.Project;
import ru.vorobyev.tracker.domain.user.Role;
import ru.vorobyev.tracker.domain.user.User;
import ru.vorobyev.tracker.exception.NotExistException;
import ru.vorobyev.tracker.repository.UserRepository;
import ru.vorobyev.tracker.repository.jdbc.BaseSqlHelper;
import ru.vorobyev.tracker.repository.jdbc.ConnectionFactory;
import ru.vorobyev.tracker.repository.jdbc.DatabaseConnection;

import java.sql.*;
import java.util.*;

@Profile("jdbc")
@Repository
public class UserJdbcRepositoryImpl implements UserRepository {

    private final ConnectionFactory connectionFactory;

    public UserJdbcRepositoryImpl() {
        this.connectionFactory = () -> DatabaseConnection.getInstance().getConnection();
    }

    @Override
    public User save(User user) {
        if (user.isNew()) {
            return new BaseSqlHelper<User>(connectionFactory) {
                @Override
                public void processing(Connection connection) throws SQLException {
                    try (PreparedStatement ps = connection.prepareStatement("INSERT INTO users(email, name, password) VALUES (?, ?, ?)", Statement.RETURN_GENERATED_KEYS)) {
                        ps.setString(1, user.getEmail());
                        ps.setString(2, user.getName());
                        ps.setString(3, user.getPassword());
                        ps.executeUpdate();

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
                    try (PreparedStatement ps = connection.prepareStatement("UPDATE users u SET email=?, name=?, password=? WHERE u.id=?")) {
                        ps.setString(1, user.getEmail());
                        ps.setString(2, user.getName());
                        ps.setString(3, user.getPassword());
                        ps.setInt(4, user.getId());
                        ps.executeUpdate();
                    }

                    deleteRoles(connection, user);
                    deleteProjectRelation(connection, user);
                    insertRoles(connection, user);
                    insertProjectRelation(connection, user);

                    setEntity(user);
                }
            }.getEntity();
        }
    }

    @Override
    public boolean delete(int id) {
        try {
            User user = get(id);
        } catch (Exception e) {
            throw new NotExistException(String.format("User with %d not exist.", id));
        }

        return new BaseSqlHelper<User>(connectionFactory) {
            @Override
            public void processing(Connection connection) throws SQLException {
                try (PreparedStatement ps = connection.prepareStatement("DELETE FROM users WHERE id=?")) {
                    ps.setInt(1, id);
                    ps.execute();

                    setResult(true);
                }
            }
        }.isResult();
    }

    @Override
    public User get(int id) {
        return new BaseSqlHelper<User>(connectionFactory) {
            @Override
            public void processing(Connection connection) throws SQLException {
                try (PreparedStatement ps = connection.prepareStatement("SELECT u.id, u.email, u.name, u.password, ur.roles FROM users u" +
                        " LEFT JOIN user_roles ur" +
                        " ON u.id = ur.user_id " +
                        " WHERE u.id=?")) {
                    ps.setInt(1, id);
                    ResultSet rs = ps.executeQuery();

                    if (!rs.next()) {
                        throw new NotExistException(String.format("User with ID:%d not exist.", id));
                    }

                    User user = addRolesToUser(rs).iterator().next();
                    addProjectsToUser(connection, user);

                    setEntity(user);
                }
            }
        }.getEntity();
    }

    @Override
    public User getByEmail(String email) {
        return new BaseSqlHelper<User>(connectionFactory) {
            @Override
            public void processing(Connection connection) throws SQLException {
                try (PreparedStatement ps = connection.prepareStatement("SELECT u.id, u.email, u.name, u.password, ur.roles FROM users u " +
                        "LEFT JOIN user_roles ur ON u.id = ur.user_id " +
                        "WHERE u.email=?")) {
                    ps.setString(1, email);
                    ResultSet rs = ps.executeQuery();

                    if (!rs.next()) {
                        throw new NotExistException(String.format("User with email:%s not exist.", email));
                    }

                    User user = addRolesToUser(rs).iterator().next();
                    addProjectsToUser(connection, user);

                    setEntity(user);
                }
            }
        }.getEntity();
    }

    @Override
    public List<User> getAll() {
         return new ArrayList<>(new BaseSqlHelper<User>(connectionFactory) {
             @Override
             public void processing(Connection connection) throws SQLException {
                 try (PreparedStatement ps = connection.prepareStatement("SELECT u.id, u.email, u.name, u.password, ur.roles FROM users u " +
                         "LEFT JOIN user_roles ur ON u.id = ur.user_id " +
                         "ORDER BY u.name")) {
                     ResultSet rs = ps.executeQuery();

                     Set<User> users = addRolesToUser(rs);

                     users.forEach(user -> {
                         try {
                             addProjectsToUser(connection, user);
                         } catch (SQLException e) {
                             e.printStackTrace();
                         }
                     });

                     setEntities(users);
                 }
             }
         }.getEntities());
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

    private void deleteProjectRelation(Connection connection, User user) throws SQLException {
        deleteAttribute(connection, user, "DELETE FROM project_users WHERE user_id=?");
    }

    private void deleteRoles(Connection connection, User user) throws SQLException {
        deleteAttribute(connection, user, "DELETE FROM user_roles WHERE user_id=?");
    }

    private void deleteAttribute(Connection connection, User user, String sql) throws SQLException {
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, user.getId());
            ps.execute();
        }
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

    private Set<User> addRolesToUser(ResultSet rs) throws SQLException {
        Map<Integer, User> map = new LinkedHashMap<>();

        if (rs.isLast()) {
            userResultSetExtractor(rs, map);
        } else {
            while (rs.next()) {
                userResultSetExtractor(rs, map);
            }
        }

        return new HashSet<>(map.values());
    }

    private void userResultSetExtractor(ResultSet rs, Map<Integer, User> map) throws SQLException {
        int id = Integer.parseInt(rs.getString("id"));
        String email = rs.getString("email");
        String name = rs.getString("name");
        String password = rs.getString("password");
        Role role = rs.getString("roles").equals("ROLE_USER") ? Role.ROLE_USER :
                rs.getString("roles").equals("ROLE_ADMIN") ? Role.ROLE_ADMIN : Role.ROLE_SYSTEM;
        User user = new User(id, name, email, password, null, role);
        map.putIfAbsent(user.getId(), user);
        map.get(user.getId()).getRoles().add(role);
    }

    private void addProjectsToUser(Connection connection, User user) throws SQLException {
        try (PreparedStatement ps = connection.prepareStatement("SELECT p.id, p.administrator, p.department, p.description, p.manager, p.name" +
                " FROM projects p, project_users pu" +
                " where p.id=pu.project_id and pu.user_id=?")) {
            ps.setInt(1, user.getId());
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                int id = Integer.parseInt(rs.getString("id"));
                String admin = rs.getString("administrator");
                String department = rs.getString("department");
                String description = rs.getString("description");
                String manager = rs.getString("manager");
                String name = rs.getString("name");

                Project project = new Project(id, name, description, department, manager, admin, null, null);
                if (user.getProjects() == null) {
                    Set<Project> prjct = new HashSet<>();
                    prjct.add(project);
                    user.setProjects(prjct);
                } else {
                    user.getProjects().add(project);
                }
            }
        }
    }
}