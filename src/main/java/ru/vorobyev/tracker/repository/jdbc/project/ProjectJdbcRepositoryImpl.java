package ru.vorobyev.tracker.repository.jdbc.project;

import ru.vorobyev.tracker.domain.project.Project;
import ru.vorobyev.tracker.repository.ProjectRepository;
import ru.vorobyev.tracker.repository.jdbc.BaseSqlHelper;
import ru.vorobyev.tracker.repository.jdbc.ConnectionFactory;
import ru.vorobyev.tracker.repository.jdbc.DatabaseConnection;

import java.sql.*;
import java.util.List;

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

                }
            }.getEntity();
        }
    }

    private void insertUserRelation(Connection connection, Project project) throws SQLException {
        try (PreparedStatement ps = connection.prepareStatement("INSERT INTO project_users(project_id, user_id) VALUES (?, ?)")) {
            project.getParticipants().forEach(user -> {

            });
        }
    }

    @Override
    public boolean delete(int id) {
        return false;
    }

    @Override
    public Project get(int id) {
        return null;
    }

    @Override
    public Project getByName(String name) {
        return null;
    }

    @Override
    public List<Project> getAll() {
        return null;
    }
}
