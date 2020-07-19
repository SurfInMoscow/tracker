package ru.vorobyev.tracker.repository.jdbc.project;

import ru.vorobyev.tracker.domain.issue.*;
import ru.vorobyev.tracker.domain.issue.workflow.WorkflowStatus;
import ru.vorobyev.tracker.domain.project.Sprint;
import ru.vorobyev.tracker.exception.NotExistException;
import ru.vorobyev.tracker.repository.SprintRepository;
import ru.vorobyev.tracker.repository.jdbc.BaseSqlHelper;
import ru.vorobyev.tracker.repository.jdbc.ConnectionFactory;
import ru.vorobyev.tracker.repository.jdbc.DatabaseConnection;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static ru.vorobyev.tracker.repository.jdbc.project.IssueTypes.*;

public class SprintJdbcRepositoryImpl implements SprintRepository {

    private final ConnectionFactory connectionFactory;

    public SprintJdbcRepositoryImpl() {
        this.connectionFactory = () -> DatabaseConnection.getInstance().getConnection();
    }

    @Override
    public Sprint save(Sprint sprint) {
        if (sprint.isNew()) {
            return new BaseSqlHelper<Sprint>(connectionFactory) {
                @Override
                public void processing(Connection connection) throws SQLException {
                    try (PreparedStatement ps = connection.prepareStatement("INSERT INTO sprint DEFAULT VALUES", Statement.RETURN_GENERATED_KEYS)) {
                        ps.executeUpdate();

                        try (ResultSet generatedKeys = ps.getGeneratedKeys()) {
                            if (generatedKeys.next()) {
                                sprint.setId(generatedKeys.getInt(1));
                            } else {
                                throw new SQLException("No ID generated.");
                            }
                        }
                    }

                    setEntity(sprint);
                }
            }.getEntity();
        }

        return sprint;
    }

    @Override
    public boolean delete(int id) {
        try {
            Sprint sprint = get(id);
        } catch (Exception e) {
            throw new NotExistException(String.format("Sprint with ID:%d not exist.", id));
        }

        return new BaseSqlHelper<Sprint>(connectionFactory) {
            @Override
            public void processing(Connection connection) throws SQLException {
                try (PreparedStatement ps = connection.prepareStatement("DELETE FROM sprint WHERE id=?")) {
                    ps.setInt(1, id);
                    ps.execute();
                }

                setResult(true);
            }
        }.isResult();
    }

    @Override
    public Sprint get(int id) {
        return new BaseSqlHelper<Sprint>(connectionFactory) {
            @Override
            public void processing(Connection connection) throws SQLException {
                try (PreparedStatement ps = connection.prepareStatement("SELECT * FROM sprint WHERE id=?")) {
                    ps.setInt(1, id);
                    ResultSet rs = ps.executeQuery();

                    if (!rs.next()) {
                        throw new NotExistException(String.format("Sprint with ID:%d not exist.", id));
                    }

                    Sprint sprint = new Sprint();
                    sprint.setId(Integer.parseInt(rs.getString("id")));

                    addBugsToSprint(connection, sprint);
                    addEpicsToSprint(connection, sprint);
                    addStoriesToSprint(connection, sprint);
                    addTasksToSprint(connection, sprint);

                    setEntity(sprint);
                }
            }
        }.getEntity();
    }

    @Override
    public List<Sprint> getAll() {
        return new ArrayList<>(new BaseSqlHelper<Sprint>(connectionFactory) {
            @Override
            public void processing(Connection connection) throws SQLException {
                try (PreparedStatement ps = connection.prepareStatement("SELECT * FROM sprint")) {
                    Set<Sprint> sprints = new HashSet<>();
                    ResultSet rs = ps.executeQuery();

                    while (rs.next()) {
                        Sprint sprint = new Sprint();
                        sprint.setId(Integer.parseInt(rs.getString("id")));
                        sprints.add(sprint);
                    }

                    sprints.forEach(backlog -> {
                        try {
                            addBugsToSprint(connection, backlog);
                            addEpicsToSprint(connection, backlog);
                            addStoriesToSprint(connection, backlog);
                            addTasksToSprint(connection, backlog);
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }
                    });

                    setEntities(sprints);
                }
            }
        }.getEntities());
    }

    public void clear() {
        new BaseSqlHelper<Sprint>(connectionFactory) {
            @Override
            public void processing(Connection connection) throws SQLException {
                try (PreparedStatement ps = connection.prepareStatement("DELETE FROM sprint")) {
                    ps.execute();
                }
            }
        };
    }

    private void addTasksToSprint(Connection connection, Sprint sprint) throws SQLException {
        try (PreparedStatement ps = connection.prepareStatement("SELECT id, creation_date, name, priority, status FROM tasks WHERE sprint_id=?")) {
            ps.setInt(1, sprint.getId());
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Task task = (Task) extractIssueFromResultSet(rs, TASK);
                sprint.getTasks().add(task);
            }
        }
    }

    private void addStoriesToSprint(Connection connection, Sprint sprint) throws SQLException {
        try (PreparedStatement ps = connection.prepareStatement("SELECT id, creation_date, name, priority, status FROM stories WHERE sprint_id=?")) {
            ps.setInt(1, sprint.getId());
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Story story = (Story) extractIssueFromResultSet(rs, STORY);
                sprint.getStories().add(story);
            }
        }
    }

    private void addEpicsToSprint(Connection connection, Sprint sprint) throws SQLException {
        try (PreparedStatement ps = connection.prepareStatement("SELECT id, creation_date, name, priority, status FROM epics WHERE sprint_id=?")) {
            ps.setInt(1, sprint.getId());
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Epic epic = (Epic) extractIssueFromResultSet(rs, EPIC);
                sprint.getEpics().add(epic);
            }
        }
    }

    private void addBugsToSprint(Connection connection, Sprint sprint) throws SQLException {
        try (PreparedStatement ps = connection.prepareStatement("SELECT id, creation_date, name, priority, status FROM bugs WHERE sprint_id=?")) {
            ps.setInt(1, sprint.getId());
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Bug bug = (Bug) extractIssueFromResultSet(rs, BUG);
                sprint.getBugs().add(bug);
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
        Priority bugPriority = priority.equals("LOW") ?
                Priority.LOW : priority.equals("MEDIUM") ?
                Priority.MEDIUM : Priority.HIGH;

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

    private WorkflowStatus getWorkflowStatus(String status) {
        switch (status) {
            case "OPEN_ISSUE":
                return WorkflowStatus.OPEN_ISSUE;
            case "IN_PROGRESS_ISSUE":
                return WorkflowStatus.IN_PROGRESS_ISSUE;
            case "REVIEW_ISSUE":
                return WorkflowStatus.REVIEW_ISSUE;
            case "TEST_ISSUE":
                return WorkflowStatus.TEST_ISSUE;
            case "RESOLVED_ISSUE":
                return WorkflowStatus.RESOLVED_ISSUE;
            case "RE_OPENED_ISSUE":
                return WorkflowStatus.RE_OPENED_ISSUE;
            case "CLOSE_ISSUE":
                return WorkflowStatus.CLOSE_ISSUE;
        }

        return null;
    }
}
