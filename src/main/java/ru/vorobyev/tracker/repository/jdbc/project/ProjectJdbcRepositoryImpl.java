package ru.vorobyev.tracker.repository.jdbc.project;

import ru.vorobyev.tracker.domain.project.Backlog;
import ru.vorobyev.tracker.domain.project.Project;
import ru.vorobyev.tracker.domain.project.Sprint;
import ru.vorobyev.tracker.domain.user.User;
import ru.vorobyev.tracker.exception.NotExistException;
import ru.vorobyev.tracker.repository.ProjectRepository;
import ru.vorobyev.tracker.repository.jdbc.BaseSqlHelper;
import ru.vorobyev.tracker.repository.jdbc.ConnectionFactory;
import ru.vorobyev.tracker.repository.jdbc.DatabaseConnection;

import java.sql.*;
import java.util.*;

public class ProjectJdbcRepositoryImpl implements ProjectRepository {

    private final ConnectionFactory connectionFactory;

    public ProjectJdbcRepositoryImpl() {
        this.connectionFactory = () -> DatabaseConnection.getInstance().getConnection();
    }

    @Override
    public Project save(Project project) {
        if (project.isNew()) {
            return new BaseSqlHelper<Project>(connectionFactory) {
                @Override
                public void processing(Connection connection) throws SQLException {
                    try (PreparedStatement ps = connection.prepareStatement("INSERT INTO projects(administrator, department, description, manager, name, backlog_id, sprint_id)" +
                            " VALUES (?, ?, ?, ?, ?, ?, ?)", Statement.RETURN_GENERATED_KEYS)) {
                        setStatementProject(ps, project);

                        ps.executeUpdate();

                        try (ResultSet generatedKeys = ps.getGeneratedKeys()) {
                            if (generatedKeys.next()) {
                                project.setId(generatedKeys.getInt(1));
                            } else {
                                throw new SQLException("No ID generated.");
                            }
                        }
                    }

                    insertUserRelation(connection, project);

                    setEntity(project);
                }
            }.getEntity();
        } else {
            return new BaseSqlHelper<Project>(connectionFactory) {
                @Override
                public void processing(Connection connection) throws SQLException {
                    try (PreparedStatement ps = connection.prepareStatement("UPDATE projects p SET administrator=?, department=?, description=?, manager=?, name=?, backlog_id=?, sprint_id=?" +
                            " WHERE p.id=?")) {
                        setStatementProject(ps, project);

                        ps.setInt(8, project.getId());

                        ps.executeUpdate();

                        deleteUserRelation(connection, project);
                        insertUserRelation(connection, project);

                        setEntity(project);
                    }

                }
            }.getEntity();
        }
    }

    @Override
    public boolean delete(int id) {
        return new BaseSqlHelper<Project>(connectionFactory) {
            @Override
            public void processing(Connection connection) throws SQLException {
                Integer backlog_id = null;
                Integer sprint_id = null;
                try (PreparedStatement ps = connection.prepareStatement("SELECT backlog_id, sprint_id FROM projects WHERE id=?")) {
                    ps.setInt(1, id);
                    ResultSet rs = ps.executeQuery();
                    while (rs.next()) {
                        backlog_id = rs.getString("backlog_id") == null ? null : Integer.parseInt(rs.getString("backlog_id"));
                        sprint_id = rs.getString("sprint_id") == null ? null : Integer.parseInt(rs.getString("sprint_id"));
                    }
                }
                try (PreparedStatement ps = connection.prepareStatement("DELETE FROM projects WHERE id=?")) {
                    ps.setInt(1, id);
                    ps.execute();

                    if (backlog_id != null)
                        deleteProjectBacklog(connection, backlog_id);

                    if (sprint_id != null)
                        deleteProjectSprint(connection, sprint_id);

                    setResult(true);
                }
            }
        }.isResult();
    }

    @Override
    public Project get(int id) {
        return new BaseSqlHelper<Project>(connectionFactory) {
            @Override
            public void processing(Connection connection) throws SQLException {
                try (PreparedStatement ps = connection.prepareStatement("SELECT p.id, p.administrator, p.department, p.description, p.manager, p.name, b.id as b_id, s.id as s_id FROM projects p " +
                                                                                                    "LEFT JOIN backlog b on p.backlog_id = b.id " +
                                                                                                    "LEFT JOIN sprint s on p.sprint_id = s.id " +
                                                                                                    "WHERE p.id=?")) {
                   ps.setInt(1, id);
                   ResultSet rs = ps.executeQuery();

                   if (!rs.next()) {
                       throw new NotExistException(String.format("Project with ID:%d not exist.", id));
                   }

                   Project project = projectsWithBacklogAndSprint(rs).iterator().next();
                   
                   addParticipantsToProject(connection, project);

                   setEntity(project);
                }
            }
        }.getEntity();
    }

    @Override
    public Project getByName(String name) {
        return new BaseSqlHelper<Project>(connectionFactory) {
            @Override
            public void processing(Connection connection) throws SQLException {
                try (PreparedStatement ps = connection.prepareStatement("SELECT p.id, p.administrator, p.department, p.description, p.manager, p.name, b.id as b_id, s.id as s_id FROM projects p " +
                        "LEFT JOIN backlog b on p.backlog_id = b.id " +
                        "LEFT JOIN sprint s on p.sprint_id = s.id " +
                        "WHERE p.name=?")) {
                    ps.setString(1, name);
                    ResultSet rs = ps.executeQuery();

                    if (!rs.next()) {
                        throw new NotExistException(String.format("Project with name:%s not exist.", name));
                    }

                    Project project = projectsWithBacklogAndSprint(rs).iterator().next();

                    addParticipantsToProject(connection, project);

                    setEntity(project);
                }
            }
        }.getEntity();
    }

    @Override
    public List<Project> getAll() {
        return new ArrayList<>(new BaseSqlHelper<Project>(connectionFactory) {
            @Override
            public void processing(Connection connection) throws SQLException {
                try (PreparedStatement ps = connection.prepareStatement("SELECT p.id, p.administrator, p.department, p.description, p.manager, p.name, b.id as b_id, s.id as s_id FROM projects p " +
                        "LEFT JOIN backlog b on p.backlog_id = b.id " +
                        "LEFT JOIN sprint s on p.sprint_id = s.id " +
                        "ORDER BY p.name")) {
                    ResultSet rs = ps.executeQuery();

                    Set<Project> projects = projectsWithBacklogAndSprint(rs);

                    projects.forEach(project -> {
                        try {
                            addParticipantsToProject(connection, project);
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }
                    });

                    setEntities(projects);
                }
            }
        }.getEntities());
    }

    private void setStatementProject (PreparedStatement ps, Project project) throws SQLException {
        ps.setString(1, project.getAdministrator());
        ps.setString(2, project.getDepartment());
        ps.setString(3, project.getDescription());
        ps.setString(4, project.getManager());
        ps.setString(5, project.getName());

        if (project.getBacklog() != null && project.getBacklog().getId() != null) {
            ps.setInt(6, project.getBacklog().getId());
        } else {
            ps.setNull(6, Types.INTEGER);
        }

        if (project.getSprint() != null && project.getSprint().getId() != null) {
            ps.setInt(7, project.getSprint().getId());
        } else {
            ps.setNull(7, Types.INTEGER);
        }
    }

    private void insertUserRelation(Connection connection, Project project) throws SQLException {
        try (PreparedStatement ps = connection.prepareStatement("INSERT INTO project_users(project_id, user_id) VALUES (?, ?)")) {
            project.getParticipants().forEach(user -> {
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

    private void deleteUserRelation(Connection connection, Project project) throws SQLException {
        try (PreparedStatement ps = connection.prepareStatement("DELETE FROM project_users WHERE project_id=?")) {
            ps.setInt(1, project.getId());
            ps.execute();
        }
    }

    private void deleteProjectSprint(Connection connection, int sprint_id) throws SQLException {
        try (PreparedStatement ps = connection.prepareStatement("DELETE FROM sprint WHERE id=?")) {
            ps.setInt(1, sprint_id);
            ps.execute();
        }
    }

    private void deleteProjectBacklog(Connection connection, int backlog_id) throws SQLException {
        try (PreparedStatement ps = connection.prepareStatement("DELETE FROM backlog WHERE id=?")) {
            ps.setInt(1, backlog_id);
            ps.execute();
        }
    }
    
    private Set<Project> projectsWithBacklogAndSprint(ResultSet rs) throws SQLException {
        Map<Integer, Project> map = new LinkedHashMap<>();

        if (rs.isLast()) {
            Project project = projectResultSetExtractor(rs);
            map.putIfAbsent(project.getId(), project);
        } else {
            while (rs.next()) {
                Project project = projectResultSetExtractor(rs);
                map.putIfAbsent(project.getId(), project);
            }
        }

        return new HashSet<>(map.values());
    }

    private Project projectResultSetExtractor(ResultSet rs) throws SQLException {
        int id = Integer.parseInt(rs.getString("id"));
        String administrator = rs.getString("administrator");
        String department = rs.getString("department");
        String description = rs.getString("description");
        String manager = rs.getString("manager");
        String name = rs.getString("name");
        Integer b_id = rs.getString("b_id") == null ? null : Integer.parseInt(rs.getString("b_id"));
        Integer s_id = rs.getString("s_id") == null ? null : Integer.parseInt(rs.getString("s_id"));
        Backlog backlog = new Backlog();
        backlog.setId(b_id);
        Sprint sprint = new Sprint();
        sprint.setId(s_id);
        return new Project(id, name, description, department, manager, administrator, backlog, sprint);
    }

    private void addParticipantsToProject(Connection connection, Project project) throws SQLException {
        try (PreparedStatement ps = connection.prepareStatement("SELECT * FROM users u, project_users pu WHERE u.id=pu.user_id AND pu.project_id=?")) {
            ps.setInt(1, project.getId());
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                int id = Integer.parseInt(rs.getString("id"));
                String email = rs.getString("email");
                String name = rs.getString("name");
                String password = rs.getString("password");
                User user = new User();
                user.setId(id);
                user.setEmail(email);
                user.setName(name);
                user.setPassword(password);

                if (project.getParticipants() == null) {
                    Set<User> participants = new HashSet<>();
                    participants.add(user);
                    project.setParticipants(participants);
                } else {
                    project.getParticipants().add(user);
                }
            }
        }
    }

    public void clear() {
        new BaseSqlHelper<Project>(connectionFactory) {
            @Override
            public void processing(Connection connection) throws SQLException {
                try (PreparedStatement ps = connection.prepareStatement("DELETE FROM projects")) {
                    ps.execute();
                }
                try (PreparedStatement ps = connection.prepareStatement("DELETE FROM backlog")) {
                    ps.execute();
                }
                try (PreparedStatement ps = connection.prepareStatement("DELETE FROM sprint")) {
                    ps.execute();
                }
            }
        };
    }
}
