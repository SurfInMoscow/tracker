package ru.vorobyev.tracker.repository.jdbc.project;

import ru.vorobyev.tracker.domain.project.Backlog;
import ru.vorobyev.tracker.repository.BacklogRepository;
import ru.vorobyev.tracker.repository.jdbc.BaseSqlHelper;
import ru.vorobyev.tracker.repository.jdbc.ConnectionFactory;
import ru.vorobyev.tracker.repository.jdbc.DatabaseConnection;

import java.sql.*;
import java.util.List;

public class BacklogJdbcRepositoryImpl implements BacklogRepository {

    private final ConnectionFactory connectionFactory;

    public BacklogJdbcRepositoryImpl() {
        this.connectionFactory = () -> DatabaseConnection.getInstance().getConnection();
    }

    @Override
    public Backlog save(Backlog backlog) {
        if (backlog.isNew()) {
            return new BaseSqlHelper<Backlog>(connectionFactory) {
                @Override
                public void processing(Connection connection) throws SQLException {
                    try (PreparedStatement ps = connection.prepareStatement("INSERT INTO backlog DEFAULT VALUES", Statement.RETURN_GENERATED_KEYS)) {
                        ps.executeUpdate();

                        try (ResultSet generatedKeys = ps.getGeneratedKeys()) {
                            if (generatedKeys.next()) {
                                backlog.setId(generatedKeys.getInt(1));
                            } else {
                                throw new SQLException("No ID generated.");
                            }
                        }
                    }

                    setEntity(backlog);
                }
            }.getEntity();
        }

        return backlog;
    }

    @Override
    public boolean delete(int id) {
        /*try {
            Backlog backlog = get(id);
        } catch (Exception e) {
            throw new NotExistException(String.format("Backlog with ID:%d not exist.", id));
        }*/

        return new BaseSqlHelper<Backlog>(connectionFactory) {
            @Override
            public void processing(Connection connection) throws SQLException {
                try (PreparedStatement ps = connection.prepareStatement("DELETE FROM backlog WHERE id=?")) {
                    ps.setInt(1, id);
                    ps.execute();
                }

                setResult(true);
            }
        }.isResult();
    }

    @Override
    public Backlog get(int id) {
        return null;
    }

    @Override
    public List<Backlog> getAll() {
        return null;
    }

    public void clear() {
        new BaseSqlHelper<Backlog>(connectionFactory) {
            @Override
            public void processing(Connection connection) throws SQLException {
                try (PreparedStatement ps = connection.prepareStatement("DELETE FROM backlog")) {
                    ps.execute();
                }
            }
        };
    }
}
