package ru.vorobyev.tracker.repository.jdbc.issue;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;
import ru.vorobyev.tracker.domain.issue.*;
import ru.vorobyev.tracker.domain.issue.workflow.WorkflowStatus;
import ru.vorobyev.tracker.domain.project.Backlog;
import ru.vorobyev.tracker.domain.project.Sprint;
import ru.vorobyev.tracker.domain.user.User;
import ru.vorobyev.tracker.exception.NotExistException;
import ru.vorobyev.tracker.repository.IssueRepository;
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

@Profile("jdbc")
@Repository
public class BugJdbcRepositoryImpl implements IssueRepository<Bug> {

    private final ConnectionFactory connectionFactory;

    public BugJdbcRepositoryImpl() {
        this.connectionFactory = () -> DatabaseConnection.getInstance().getConnection();
    }

    @Override
    public Bug save(Bug bug) {
        if (bug.isNew()) {
            return new BaseSqlHelper<Bug>(connectionFactory) {
                @Override
                public void processing(Connection connection) throws SQLException {
                    try (PreparedStatement ps = connection.prepareStatement("INSERT INTO bugs(creation_date, name, priority, status, executor_id, reporter_id, " +
                                                                                                    "backlog_id, root_epic_id, root_story_id, root_task_id, sprint_id) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)",
                                                                                    Statement.RETURN_GENERATED_KEYS)) {
                        setStatementBug(ps, bug);

                        ps.executeUpdate();

                        try (ResultSet generatedKeys = ps.getGeneratedKeys()) {
                            if (generatedKeys.next()) {
                                bug.setId(generatedKeys.getInt(1));
                            } else {
                                throw new SQLException("No ID generated.");
                            }
                        }

                        setEntity(bug);
                    }
                }
            }.getEntity();
        } else {
            return new BaseSqlHelper<Bug>(connectionFactory) {
                @Override
                public void processing(Connection connection) throws SQLException {
                    try (PreparedStatement ps = connection.prepareStatement("UPDATE bugs SET creation_date=?, name=?, priority=?, status=?, executor_id=?, reporter_id=?, " +
                            "backlog_id=?, root_epic_id=?, root_story_id=?, root_task_id=?, sprint_id=? WHERE id=?")) {
                        setStatementBug(ps, bug);
                        ps.setInt(12, bug.getId());

                        ps.executeUpdate();

                        setEntity(bug);
                    }
                }
            }.getEntity();
        }
    }

    @Override
    public boolean delete(int id) {
        return new BaseSqlHelper<Bug>(connectionFactory) {
            @Override
            public void processing(Connection connection) throws SQLException {
                try (PreparedStatement ps = connection.prepareStatement("DELETE FROM bugs WHERE id=?")) {
                    ps.setInt(1, id);
                    ps.execute();

                    setResult(true);
                }
            }
        }.isResult();
    }

    @Override
    public Bug get(int id) {
        return new BaseSqlHelper<Bug>(connectionFactory) {
            @Override
            public void processing(Connection connection) throws SQLException {
                try (PreparedStatement ps = connection.prepareStatement("SELECT b.id, b.creation_date, b.name, b.priority, b.status, " +
                        "ex.id as ex_id, ex.email as ex_e, ex.name as ex_n, ex.password as ex_p, " +
                        "re.id as re_id, re.email as re_e, re.name as re_n, re.password as re_p, " +
                        "bl.id as bl_id, " +
                        "s.id as s_id, " +
                        "e.id as e_id, e.creation_date as e_creation, e.name as e_name, e.priority as e_prio, e.status as e_sts, " +
                        "str.id as str_id, str.creation_date as str_creation, str.name as str_name, str.priority as str_prio, str.status as str_sts, " +
                        "t.id as t_id, t.creation_date as t_creation, t.name as t_name, t.priority as t_prio, t.status as t_sts " +
                        "FROM bugs b " +
                        "LEFT JOIN users ex ON b.executor_id = ex.id " +
                        "LEFT JOIN users re ON b.reporter_id = re.id " +
                        "LEFT JOIN backlog bl ON b.backlog_id = bl.id " +
                        "LEFT JOIN sprint s ON b.sprint_id = s.id " +
                        "LEFT JOIN epics e ON b.root_epic_id = e.id " +
                        "LEFT JOIN stories str ON b.root_story_id = str.id " +
                        "LEFT JOIN tasks t ON b.root_task_id = t.id " +
                        "WHERE b.id=?")) {
                    ps.setInt(1, id);
                    ResultSet rs = ps.executeQuery();

                    if (!rs.next()) {
                        throw new NotExistException(String.format("Bug with ID:%d not exist.", id));
                    }

                    Bug bug = bugResultSetExtractor(rs);

                    setEntity(bug);
                }
            }
        }.getEntity();
    }

    @Override
    public Bug getByName(String name) {
        return new BaseSqlHelper<Bug>(connectionFactory) {
            @Override
            public void processing(Connection connection) throws SQLException {
                try (PreparedStatement ps = connection.prepareStatement("SELECT b.id, b.creation_date, b.name, b.priority, b.status, " +
                        "ex.id as ex_id, ex.email as ex_e, ex.name as ex_n, ex.password as ex_p, " +
                        "re.id as re_id, re.email as re_e, re.name as re_n, re.password as re_p, " +
                        "bl.id as bl_id, " +
                        "s.id as s_id, " +
                        "e.id as e_id, e.creation_date as e_creation, e.name as e_name, e.priority as e_prio, e.status as e_sts, " +
                        "str.id as str_id, str.creation_date as str_creation, str.name as str_name, str.priority as str_prio, str.status as str_sts, " +
                        "t.id as t_id, t.creation_date as t_creation, t.name as t_name, t.priority as t_prio, t.status as t_sts " +
                        "FROM bugs b " +
                        "LEFT JOIN users ex ON b.executor_id = ex.id " +
                        "LEFT JOIN users re ON b.reporter_id = re.id " +
                        "LEFT JOIN backlog bl ON b.backlog_id = bl.id " +
                        "LEFT JOIN sprint s ON b.sprint_id = s.id " +
                        "LEFT JOIN epics e ON b.root_epic_id = e.id " +
                        "LEFT JOIN stories str ON b.root_story_id = str.id " +
                        "LEFT JOIN tasks t ON b.root_task_id = t.id " +
                        "WHERE b.name=?")) {
                    ps.setString(1, name);
                    ResultSet rs = ps.executeQuery();

                    if (!rs.next()) {
                        throw new NotExistException(String.format("Bug with name:%s not exist.", name));
                    }

                    Bug bug = bugResultSetExtractor(rs);

                    setEntity(bug);
                }
            }
        }.getEntity();
    }

    @Override
    public List<Bug> getAll() {
        return new ArrayList<>(new BaseSqlHelper<Bug>(connectionFactory) {
            @Override
            public void processing(Connection connection) throws SQLException {
                try (PreparedStatement ps = connection.prepareStatement("SELECT b.id, b.creation_date, b.name, b.priority, b.status, " +
                        "ex.id as ex_id, ex.email as ex_e, ex.name as ex_n, ex.password as ex_p, " +
                        "re.id as re_id, re.email as re_e, re.name as re_n, re.password as re_p, " +
                        "bl.id as bl_id, " +
                        "s.id as s_id, " +
                        "e.id as e_id, e.creation_date as e_creation, e.name as e_name, e.priority as e_prio, e.status as e_sts, " +
                        "str.id as str_id, str.creation_date as str_creation, str.name as str_name, str.priority as str_prio, str.status as str_sts, " +
                        "t.id as t_id, t.creation_date as t_creation, t.name as t_name, t.priority as t_prio, t.status as t_sts " +
                        "FROM bugs b " +
                        "LEFT JOIN users ex ON b.executor_id = ex.id " +
                        "LEFT JOIN users re ON b.reporter_id = re.id " +
                        "LEFT JOIN backlog bl ON b.backlog_id = bl.id " +
                        "LEFT JOIN sprint s ON b.sprint_id = s.id " +
                        "LEFT JOIN epics e ON b.root_epic_id = e.id " +
                        "LEFT JOIN stories str ON b.root_story_id = str.id " +
                        "LEFT JOIN tasks t ON b.root_task_id = t.id " +
                        "ORDER BY b.creation_date")) {
                    ResultSet rs = ps.executeQuery();

                    Set<Bug> bugs = new HashSet<>();

                    while (rs.next()) {
                        Bug bug = bugResultSetExtractor(rs);
                        bugs.add(bug);
                    }

                    setEntities(bugs);
                }
            }
        }.getEntities());
    }

    public void clear() {
        new BaseSqlHelper<Bug>(connectionFactory) {
            @Override
            public void processing(Connection connection) throws SQLException {
                try (PreparedStatement ps = connection.prepareStatement("DELETE FROM bugs")){
                    ps.execute();
                }
            }
        };
    }

    private void setStatementBug(PreparedStatement ps, Bug bug) throws SQLException {
        ps.setTimestamp(1, Timestamp.valueOf(bug.getCreationDate()));
        ps.setString(2, bug.getName());
        ps.setString(3, bug.getPriority().name());
        ps.setString(4, bug.getStatus().name());

        if (bug.getExecutor() != null && bug.getExecutor().getId() != null) {
            ps.setInt(5, bug.getExecutor().getId());
        } else {
            ps.setNull(5, Types.INTEGER);
        }

        if (bug.getReporter() != null && bug.getReporter().getId() != null) {
            ps.setInt(6, bug.getReporter().getId());
        } else {
            ps.setNull(6, Types.INTEGER);
        }

        if (bug.getBacklog() != null && bug.getBacklog().getId() != null) {
            ps.setInt(7, bug.getBacklog().getId());
        } else {
            ps.setNull(7, Types.INTEGER);
        }

        if (bug.getRootEpic() != null && bug.getRootEpic().getId() != null) {
            ps.setInt(8, bug.getRootEpic().getId());
        } else {
            ps.setNull(8, Types.INTEGER);
        }

        if (bug.getRootStory() != null && bug.getRootStory().getId() != null) {
            ps.setInt(9, bug.getRootStory().getId());
        } else {
            ps.setNull(9, Types.INTEGER);
        }

        if (bug.getRootTask() != null && bug.getRootTask().getId() != null) {
            ps.setInt(10, bug.getRootTask().getId());
        } else {
            ps.setNull(10, Types.INTEGER);
        }

        if (bug.getSprint() != null && bug.getSprint().getId() != null) {
            ps.setInt(11, bug.getSprint().getId());
        } else {
            ps.setNull(11, Types.INTEGER);
        }
    }

    private Bug bugResultSetExtractor(ResultSet rs) throws SQLException {
        int id = rs.getInt("id");
        LocalDateTime creation_date = rs.getTimestamp("creation_date").toLocalDateTime();
        String name = rs.getString("name");
        String priority = rs.getString("priority");
        Priority bugPriority = getPriority(priority);
        WorkflowStatus status = getWorkflowStatus(rs.getString("status"));
        Bug bug = new Bug(bugPriority, creation_date, name, null, null, status);
        bug.setId(id);

        int ex_id = rs.getInt("ex_id");
        User executor;
        if (ex_id != 0) {
            String ex_email = rs.getString("ex_e");
            String ex_name = rs.getString("ex_n");
            String ex_password = rs.getString("ex_p");
            executor = new User();
            executor.setName(ex_name);
            executor.setEmail(ex_email);
            executor.setPassword(ex_password);
            bug.setExecutor(executor);
        }

        int re_id = rs.getInt("re_id");
        User reporter;
        if (re_id != 0) {
            String re_email = rs.getString("re_e");
            String re_name = rs.getString("re_n");
            String re_password = rs.getString("re_p");
            reporter = new User();
            reporter.setName(re_name);
            reporter.setEmail(re_email);
            reporter.setPassword(re_password);
            bug.setReporter(reporter);
        }

        int bl_id = rs.getInt("bl_id");
        if (bl_id != 0) {
            Backlog backlog = new Backlog();
            backlog.setId(bl_id);
            bug.setBacklog(backlog);
        }

        int s_id = rs.getInt("s_id");
        if (s_id != 0) {
            Sprint sprint = new Sprint();
            sprint.setId(s_id);
            bug.setSprint(sprint);
        }

        int e_id = rs.getInt("e_id");
        if (e_id != 0) {
            LocalDateTime epicCreation = rs.getTimestamp("e_creation").toLocalDateTime();
            String epicName = rs.getString("e_name");
            String epicPriorityString = rs.getString("e_prio");
            Priority epicPriority = getPriority(epicPriorityString);
            WorkflowStatus epicStatus = getWorkflowStatus(rs.getString("e_sts"));
            Epic rootEpic = new Epic(epicPriority, epicCreation, epicName, null, null, epicStatus);
            rootEpic.setId(e_id);
            bug.setRootEpic(rootEpic);
        }

        int str_id = rs.getInt("str_id");
        if (str_id != 0) {
            LocalDateTime storyCreation = rs.getTimestamp("str_creation").toLocalDateTime();
            String storyName = rs.getString("str_name");
            String storyPriorityString = rs.getString("str_prio");
            Priority storyPriority = getPriority(storyPriorityString);
            WorkflowStatus storyStatus = getWorkflowStatus(rs.getString("str_sts"));
            Story rootStory = new Story(storyPriority, storyCreation, storyName, null, null, storyStatus);
            rootStory.setId(str_id);
            bug.setRootStory(rootStory);
        }

        int t_id = rs.getInt("t_id");
        if (t_id != 0) {
            LocalDateTime taskCreation = rs.getTimestamp("t_creation").toLocalDateTime();
            String taskName = rs.getString("t_name");
            String taskPriorityString = rs.getString("t_prio");
            Priority taskPriority = getPriority(taskPriorityString);
            WorkflowStatus taskStatus = getWorkflowStatus(rs.getString("t_sts"));
            Task rootTask = new Task(taskPriority, taskCreation, taskName, null, null, taskStatus);
            rootTask.setId(t_id);
            bug.setRootTask(rootTask);
        }

        return bug;
    }
}
