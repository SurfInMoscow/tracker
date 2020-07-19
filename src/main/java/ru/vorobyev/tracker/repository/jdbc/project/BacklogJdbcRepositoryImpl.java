package ru.vorobyev.tracker.repository.jdbc.project;

import ru.vorobyev.tracker.domain.issue.*;
import ru.vorobyev.tracker.domain.issue.workflow.WorkflowStatus;
import ru.vorobyev.tracker.domain.project.Backlog;
import ru.vorobyev.tracker.exception.NotExistException;
import ru.vorobyev.tracker.repository.FilterBacklogRepository;
import ru.vorobyev.tracker.repository.jdbc.BaseSqlHelper;
import ru.vorobyev.tracker.repository.jdbc.ConnectionFactory;
import ru.vorobyev.tracker.repository.jdbc.DatabaseConnection;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static ru.vorobyev.tracker.repository.jdbc.JdbcRepoUtils.getPriority;
import static ru.vorobyev.tracker.repository.jdbc.JdbcRepoUtils.getWorkflowStatus;
import static ru.vorobyev.tracker.repository.jdbc.project.IssueTypes.*;

public class BacklogJdbcRepositoryImpl implements FilterBacklogRepository {

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
        try {
            Backlog backlog = get(id);
        } catch (Exception e) {
            throw new NotExistException(String.format("Backlog with ID:%d not exist.", id));
        }

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
        return new BaseSqlHelper<Backlog>(connectionFactory) {
            @Override
            public void processing(Connection connection) throws SQLException {
                try (PreparedStatement ps = connection.prepareStatement("SELECT * FROM backlog WHERE id=?")) {
                    ps.setInt(1, id);
                    ResultSet rs = ps.executeQuery();

                    if (!rs.next()) {
                        throw new NotExistException(String.format("Backlog with ID:%d not exist.", id));
                    }

                    Backlog backlog = new Backlog();
                    backlog.setId(Integer.parseInt(rs.getString("id")));

                    addBugsToBacklog(connection, backlog);
                    addEpicsToBacklog(connection, backlog);
                    addStoriesToBacklog(connection, backlog);
                    addTasksToBacklog(connection, backlog);

                    setEntity(backlog);
                }
            }
        }.getEntity();
    }

    @Override
    public List<Backlog> getAll() {
        return new ArrayList<>(new BaseSqlHelper<Backlog>(connectionFactory) {
            @Override
            public void processing(Connection connection) throws SQLException {
                try (PreparedStatement ps = connection.prepareStatement("SELECT * FROM backlog")) {
                    Set<Backlog> backlogs = new HashSet<>();
                    ResultSet rs = ps.executeQuery();

                    while (rs.next()) {
                        Backlog backlog = new Backlog();
                        backlog.setId(Integer.parseInt(rs.getString("id")));
                        backlogs.add(backlog);
                    }

                    backlogs.forEach(backlog -> {
                        try {
                            addBugsToBacklog(connection, backlog);
                            addEpicsToBacklog(connection, backlog);
                            addStoriesToBacklog(connection, backlog);
                            addTasksToBacklog(connection, backlog);
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }
                    });

                    setEntities(backlogs);
                }
            }
        }.getEntities());
    }

    @Override
    public Backlog getWithIssuesByPrioriTy(int id, String priority) {
        return new BaseSqlHelper<Backlog>(connectionFactory) {
            @Override
            public void processing(Connection connection) throws SQLException {
                try (PreparedStatement ps = connection.prepareStatement("SELECT * FROM backlog WHERE id=?")) {
                    ps.setInt(1, id);
                    ResultSet rs = ps.executeQuery();

                    if (!rs.next()) {
                        throw new NotExistException(String.format("Backlog with ID:%d not exist.", id));
                    }

                    Backlog backlog = new Backlog();
                    backlog.setId(Integer.parseInt(rs.getString("id")));

                    addBugsToBacklogPriority(connection, backlog, priority);
                    addEpicsToBacklogPriority(connection, backlog, priority);
                    addStoriesToBacklogPriority(connection, backlog, priority);
                    addTasksToBacklogPriority(connection, backlog, priority);

                    setEntity(backlog);
                }
            }
        }.getEntity();
    }

    @Override
    public Backlog getWithIssuesBetweenDates(int id, LocalDateTime startDate, LocalDateTime endDate) {
        return new BaseSqlHelper<Backlog>(connectionFactory) {
            @Override
            public void processing(Connection connection) throws SQLException {
                try (PreparedStatement ps = connection.prepareStatement("SELECT * FROM backlog WHERE id=?")) {
                    ps.setInt(1, id);
                    ResultSet rs = ps.executeQuery();

                    if (!rs.next()) {
                        throw new NotExistException(String.format("Backlog with ID:%d not exist.", id));
                    }

                    Backlog backlog = new Backlog();
                    backlog.setId(Integer.parseInt(rs.getString("id")));

                    addBugsToBacklogBetweenDates(connection, backlog, startDate, endDate);
                    addEpicsToBacklogBetweenDates(connection, backlog, startDate, endDate);
                    addStoriesToBacklogBetweenDates(connection, backlog, startDate, endDate);
                    addTasksToBacklogBetweenDates(connection, backlog, startDate, endDate);

                    setEntity(backlog);
                }
            }
        }.getEntity();
    }

    @Override
    public Backlog getWithIssuesByName(int id, String name) {
        return new BaseSqlHelper<Backlog>(connectionFactory) {
            @Override
            public void processing(Connection connection) throws SQLException {
                try (PreparedStatement ps = connection.prepareStatement("SELECT * FROM backlog WHERE id=?")) {
                    ps.setInt(1, id);
                    ResultSet rs = ps.executeQuery();

                    if (!rs.next()) {
                        throw new NotExistException(String.format("Backlog with ID:%d not exist.", id));
                    }

                    Backlog backlog = new Backlog();
                    backlog.setId(Integer.parseInt(rs.getString("id")));

                    addBugsToBacklogByName(connection, backlog, name);
                    addEpicsToBacklogByName(connection, backlog, name);
                    addStoriesToBacklogByName(connection, backlog, name);
                    addTasksToBacklogByName(connection, backlog, name);

                    setEntity(backlog);
                }
            }
        }.getEntity();
    }

    @Override
    public Backlog getWithIssuesByExecutor(int id, int executor_id) {
        return new BaseSqlHelper<Backlog>(connectionFactory) {
            @Override
            public void processing(Connection connection) throws SQLException {
                try (PreparedStatement ps = connection.prepareStatement("SELECT * FROM backlog WHERE id=?")) {
                    ps.setInt(1, id);
                    ResultSet rs = ps.executeQuery();

                    if (!rs.next()) {
                        throw new NotExistException(String.format("Backlog with ID:%d not exist.", id));
                    }

                    Backlog backlog = new Backlog();
                    backlog.setId(Integer.parseInt(rs.getString("id")));

                    addBugsToBacklogByExecutor(connection, backlog, executor_id);
                    addEpicsToBacklogByExecutor(connection, backlog, executor_id);
                    addStoriesToBacklogByExecutor(connection, backlog, executor_id);
                    addTasksToBacklogByExecutor(connection, backlog, executor_id);

                    setEntity(backlog);
                }
            }
        }.getEntity();
    }

    @Override
    public Backlog getWithIssuesByReporter(int id, int reporter_id) {
        return new BaseSqlHelper<Backlog>(connectionFactory) {
            @Override
            public void processing(Connection connection) throws SQLException {
                try (PreparedStatement ps = connection.prepareStatement("SELECT * FROM backlog WHERE id=?")) {
                    ps.setInt(1, id);
                    ResultSet rs = ps.executeQuery();

                    if (!rs.next()) {
                        throw new NotExistException(String.format("Backlog with ID:%d not exist.", id));
                    }

                    Backlog backlog = new Backlog();
                    backlog.setId(Integer.parseInt(rs.getString("id")));

                    addBugsToBacklogByReporter(connection, backlog, reporter_id);
                    addEpicsToBacklogReporter(connection, backlog, reporter_id);
                    addStoriesToBacklogReporter(connection, backlog, reporter_id);
                    addTasksToBacklogReporter(connection, backlog, reporter_id);

                    setEntity(backlog);
                }
            }
        }.getEntity();
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

    private void addTasksToBacklog(Connection connection, Backlog backlog) throws SQLException {
        try (PreparedStatement ps = connection.prepareStatement("SELECT id, creation_date, name, priority, status FROM tasks WHERE backlog_id=?")) {
            ps.setInt(1, backlog.getId());
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Task task = (Task) extractIssueFromResultSet(rs, TASK);
                backlog.getTasks().add(task);
            }
        }
    }

    private void addStoriesToBacklog(Connection connection, Backlog backlog) throws SQLException {
        try (PreparedStatement ps = connection.prepareStatement("SELECT id, creation_date, name, priority, status FROM stories WHERE backlog_id=?")) {
            ps.setInt(1, backlog.getId());
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Story story = (Story) extractIssueFromResultSet(rs, STORY);
                backlog.getStories().add(story);
            }
        }
    }

    private void addEpicsToBacklog(Connection connection, Backlog backlog) throws SQLException {
        try (PreparedStatement ps = connection.prepareStatement("SELECT id, creation_date, name, priority, status FROM epics WHERE backlog_id=?")) {
            ps.setInt(1, backlog.getId());
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Epic epic = (Epic) extractIssueFromResultSet(rs, EPIC);
                backlog.getEpics().add(epic);
            }
        }
    }

    private void addBugsToBacklog(Connection connection, Backlog backlog) throws SQLException {
        try (PreparedStatement ps = connection.prepareStatement("SELECT id, creation_date, name, priority, status FROM bugs WHERE backlog_id=?")) {
            ps.setInt(1, backlog.getId());
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Bug bug = (Bug) extractIssueFromResultSet(rs, BUG);
                backlog.getBugs().add(bug);
            }
        }
    }

    private void addTasksToBacklogPriority(Connection connection, Backlog backlog, String priority) throws SQLException {
        try (PreparedStatement ps = connection.prepareStatement("SELECT id, creation_date, name, priority, status FROM tasks WHERE backlog_id=? AND priority=?")) {
            setStatementString(ps, backlog, priority);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Task task = (Task) extractIssueFromResultSet(rs, TASK);
                backlog.getTasks().add(task);
            }
        }
    }

    private void addStoriesToBacklogPriority(Connection connection, Backlog backlog, String priority) throws SQLException {
        try (PreparedStatement ps = connection.prepareStatement("SELECT id, creation_date, name, priority, status FROM stories WHERE backlog_id=? AND priority=?")) {
            setStatementString(ps, backlog, priority);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Story story = (Story) extractIssueFromResultSet(rs, STORY);
                backlog.getStories().add(story);
            }
        }
    }

    private void addEpicsToBacklogPriority(Connection connection, Backlog backlog, String priority) throws SQLException {
        try (PreparedStatement ps = connection.prepareStatement("SELECT id, creation_date, name, priority, status FROM epics WHERE backlog_id=? AND priority=?")) {
            setStatementString(ps, backlog, priority);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Epic epic = (Epic) extractIssueFromResultSet(rs, EPIC);
                backlog.getEpics().add(epic);
            }
        }
    }

    private void addBugsToBacklogPriority(Connection connection, Backlog backlog, String priority) throws SQLException {
        try (PreparedStatement ps = connection.prepareStatement("SELECT id, creation_date, name, priority, status FROM bugs WHERE backlog_id=? AND priority=?")) {
            setStatementString(ps, backlog, priority);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Bug bug = (Bug) extractIssueFromResultSet(rs, BUG);
                backlog.getBugs().add(bug);
            }
        }
    }

    private void setStatementString(PreparedStatement ps, Backlog backlog, String priority) throws SQLException {
        ps.setInt(1, backlog.getId());
        ps.setString(2, priority);
    }

    private void addStoriesToBacklogBetweenDates(Connection connection, Backlog backlog, LocalDateTime startDate, LocalDateTime endDate) throws SQLException {
        try (PreparedStatement ps = connection.prepareStatement("SELECT id, creation_date, name, priority, status FROM stories WHERE backlog_id=? AND creation_date BETWEEN ? AND ?")) {
            setStatementBetweenDates(ps, backlog, startDate, endDate);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Story story = (Story) extractIssueFromResultSet(rs, STORY);
                backlog.getStories().add(story);
            }
        }
    }

    private void addBugsToBacklogBetweenDates(Connection connection, Backlog backlog, LocalDateTime startDate, LocalDateTime endDate) throws SQLException {
        try (PreparedStatement ps = connection.prepareStatement("SELECT id, creation_date, name, priority, status FROM bugs WHERE backlog_id=? AND creation_date BETWEEN ? AND ?")) {
            setStatementBetweenDates(ps, backlog, startDate, endDate);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Bug bug = (Bug) extractIssueFromResultSet(rs, BUG);
                backlog.getBugs().add(bug);
            }
        }
    }

    private void addEpicsToBacklogBetweenDates(Connection connection, Backlog backlog, LocalDateTime startDate, LocalDateTime endDate) throws SQLException {
        try (PreparedStatement ps = connection.prepareStatement("SELECT id, creation_date, name, priority, status FROM epics WHERE backlog_id=? AND creation_date BETWEEN ? AND ?")) {
            setStatementBetweenDates(ps, backlog, startDate, endDate);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Epic epic = (Epic) extractIssueFromResultSet(rs, EPIC);
                backlog.getEpics().add(epic);
            }
        }
    }

    private void addTasksToBacklogBetweenDates(Connection connection, Backlog backlog, LocalDateTime startDate, LocalDateTime endDate) throws SQLException {
        try (PreparedStatement ps = connection.prepareStatement("SELECT id, creation_date, name, priority, status FROM tasks WHERE backlog_id=? AND creation_date BETWEEN ? AND ?")) {
            setStatementBetweenDates(ps, backlog, startDate, endDate);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Task task = (Task) extractIssueFromResultSet(rs, TASK);
                backlog.getTasks().add(task);
            }
        }
    }

    private void setStatementBetweenDates(PreparedStatement ps, Backlog backlog, LocalDateTime startDate, LocalDateTime endDate) throws SQLException {
        ps.setInt(1, backlog.getId());
        ps.setTimestamp(2, Timestamp.valueOf(startDate));
        ps.setTimestamp(3, Timestamp.valueOf(endDate));
    }

    private void addTasksToBacklogByName(Connection connection, Backlog backlog, String name) throws SQLException {
        try (PreparedStatement ps = connection.prepareStatement("SELECT id, creation_date, name, priority, status FROM tasks WHERE backlog_id=? AND name=?")) {
            setStatementString(ps, backlog, name);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Task task = (Task) extractIssueFromResultSet(rs, TASK);
                backlog.getTasks().add(task);
            }
        }
    }

    private void addStoriesToBacklogByName(Connection connection, Backlog backlog, String name) throws SQLException {
        try (PreparedStatement ps = connection.prepareStatement("SELECT id, creation_date, name, priority, status FROM stories WHERE backlog_id=? AND name=?")) {
            setStatementString(ps, backlog, name);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Story story = (Story) extractIssueFromResultSet(rs, STORY);
                backlog.getStories().add(story);
            }
        }
    }

    private void addEpicsToBacklogByName(Connection connection, Backlog backlog, String name) throws SQLException {
        try (PreparedStatement ps = connection.prepareStatement("SELECT id, creation_date, name, priority, status FROM epics WHERE backlog_id=? AND name=?")) {
            setStatementString(ps, backlog, name);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Epic epic = (Epic) extractIssueFromResultSet(rs, EPIC);
                backlog.getEpics().add(epic);
            }
        }
    }

    private void addBugsToBacklogByName(Connection connection, Backlog backlog, String name) throws SQLException {
        try (PreparedStatement ps = connection.prepareStatement("SELECT id, creation_date, name, priority, status FROM bugs WHERE backlog_id=? AND name=?")) {
            setStatementString(ps, backlog, name);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Bug bug = (Bug) extractIssueFromResultSet(rs, BUG);
                backlog.getBugs().add(bug);
            }
        }
    }

    private void addTasksToBacklogByExecutor(Connection connection, Backlog backlog, int executor_id) throws SQLException {
        try (PreparedStatement ps = connection.prepareStatement("SELECT id, creation_date, name, priority, status FROM tasks WHERE backlog_id=? AND executor_id=?")) {
            setStatementActor(ps, backlog, executor_id);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Task task = (Task) extractIssueFromResultSet(rs, TASK);
                backlog.getTasks().add(task);
            }
        }
    }

    private void addStoriesToBacklogByExecutor(Connection connection, Backlog backlog, int executor_id) throws SQLException {
        try (PreparedStatement ps = connection.prepareStatement("SELECT id, creation_date, name, priority, status FROM stories WHERE backlog_id=? AND executor_id=?")) {
            setStatementActor(ps, backlog, executor_id);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Story story = (Story) extractIssueFromResultSet(rs, STORY);
                backlog.getStories().add(story);
            }
        }
    }

    private void addEpicsToBacklogByExecutor(Connection connection, Backlog backlog, int executor_id) throws SQLException {
        try (PreparedStatement ps = connection.prepareStatement("SELECT id, creation_date, name, priority, status FROM epics WHERE backlog_id=? AND executor_id=?")) {
            setStatementActor(ps, backlog, executor_id);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Epic epic = (Epic) extractIssueFromResultSet(rs, EPIC);
                backlog.getEpics().add(epic);
            }
        }
    }

    private void addBugsToBacklogByExecutor(Connection connection, Backlog backlog, int executor_id) throws SQLException {
        try (PreparedStatement ps = connection.prepareStatement("SELECT id, creation_date, name, priority, status FROM bugs WHERE backlog_id=? AND executor_id=?")) {
            setStatementActor(ps, backlog, executor_id);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Bug bug = (Bug) extractIssueFromResultSet(rs, BUG);
                backlog.getBugs().add(bug);
            }
        }
    }

    private void setStatementActor(PreparedStatement ps, Backlog backlog, int executor_id) throws SQLException {
        ps.setInt(1, backlog.getId());
        ps.setInt(2, executor_id);
    }

    private void addTasksToBacklogReporter(Connection connection, Backlog backlog, int reporter_id) throws SQLException {
        try (PreparedStatement ps = connection.prepareStatement("SELECT id, creation_date, name, priority, status FROM tasks WHERE backlog_id=? AND reporter_id=?")) {
            setStatementActor(ps, backlog, reporter_id);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Task task = (Task) extractIssueFromResultSet(rs, TASK);
                backlog.getTasks().add(task);
            }
        }
    }

    private void addStoriesToBacklogReporter(Connection connection, Backlog backlog, int reporter_id) throws SQLException {
        try (PreparedStatement ps = connection.prepareStatement("SELECT id, creation_date, name, priority, status FROM stories WHERE backlog_id=? AND reporter_id=?")) {
            setStatementActor(ps, backlog, reporter_id);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Story story = (Story) extractIssueFromResultSet(rs, STORY);
                backlog.getStories().add(story);
            }
        }
    }

    private void addEpicsToBacklogReporter(Connection connection, Backlog backlog, int reporter_id) throws SQLException {
        try (PreparedStatement ps = connection.prepareStatement("SELECT id, creation_date, name, priority, status FROM epics WHERE backlog_id=? AND reporter_id=?")) {
            setStatementActor(ps, backlog, reporter_id);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Epic epic = (Epic) extractIssueFromResultSet(rs, EPIC);
                backlog.getEpics().add(epic);
            }
        }
    }

    private void addBugsToBacklogByReporter(Connection connection, Backlog backlog, int reporter_id) throws SQLException {
        try (PreparedStatement ps = connection.prepareStatement("SELECT id, creation_date, name, priority, status FROM bugs WHERE backlog_id=? AND reporter_id=?")) {
            setStatementActor(ps, backlog, reporter_id);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Bug bug = (Bug) extractIssueFromResultSet(rs, BUG);
                backlog.getBugs().add(bug);
            }
        }
    }

    private Issue extractIssueFromResultSet(ResultSet rs, IssueTypes type) throws SQLException {
        int id = Integer.parseInt(rs.getString("id"));
        LocalDateTime creation_date = rs.getTimestamp("creation_date").toLocalDateTime();
        String name = rs.getString("name");
        String priority = rs.getString("priority");
        String status = rs.getString("status");
        WorkflowStatus workflowStatus = getWorkflowStatus(status);
        Priority bugPriority = getPriority(priority);

        switch (type) {
            case BUG:
                Bug bug = new Bug(bugPriority, creation_date, name, null, null, workflowStatus);
                bug.setId(id);
                return bug;
            case EPIC:
                Epic epic = new Epic(bugPriority, creation_date, name, null, null, workflowStatus);
                epic.setId(id);
                return epic;
            case STORY:
                Story story = new Story(bugPriority, creation_date, name, null, null, workflowStatus);
                story.setId(id);
                return story;
            case TASK:
                Task task = new Task(bugPriority, creation_date, name, null, null, workflowStatus);
                task.setId(id);
                return task;
        }

        return null;
    }
}