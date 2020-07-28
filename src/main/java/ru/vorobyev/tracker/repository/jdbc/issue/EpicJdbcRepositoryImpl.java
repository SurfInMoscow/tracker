package ru.vorobyev.tracker.repository.jdbc.issue;

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

@Repository
public class EpicJdbcRepositoryImpl implements IssueRepository<Epic> {

    private final ConnectionFactory connectionFactory;

    public EpicJdbcRepositoryImpl() {
        this.connectionFactory = () -> DatabaseConnection.getInstance().getConnection();
    }

    @Override
    public Epic save(Epic epic) {
        if (epic.isNew()) {
            return new BaseSqlHelper<Epic>(connectionFactory) {
                @Override
                public void processing(Connection connection) throws SQLException {
                    try (PreparedStatement ps = connection.prepareStatement("INSERT INTO epics(creation_date, name, priority, status, executor_id, reporter_id, " +
                                    "backlog_id, root_bug_id, root_story_id, root_task_id, sprint_id) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)",
                            Statement.RETURN_GENERATED_KEYS)) {
                        setStatementEpic(ps, epic);

                        ps.executeUpdate();

                        try (ResultSet generatedKeys = ps.getGeneratedKeys()) {
                            if (generatedKeys.next()) {
                                epic.setId(generatedKeys.getInt(1));
                            } else {
                                throw new SQLException("No ID generated.");
                            }
                        }

                        setEntity(epic);
                    }
                }
            }.getEntity();
        } else {
            return new BaseSqlHelper<Epic>(connectionFactory) {
                @Override
                public void processing(Connection connection) throws SQLException {
                    try (PreparedStatement ps = connection.prepareStatement("UPDATE epics SET creation_date=?, name=?, priority=?, status=?, executor_id=?, reporter_id=?, " +
                            "backlog_id=?, root_bug_id=?, root_story_id=?, root_task_id=?, sprint_id=? WHERE id=?")) {
                        setStatementEpic(ps, epic);
                        ps.setInt(12, epic.getId());

                        ps.executeUpdate();

                        setEntity(epic);
                    }
                }
            }.getEntity();
        }
    }

    @Override
    public boolean delete(int id) {
        return new BaseSqlHelper<Epic>(connectionFactory) {
            @Override
            public void processing(Connection connection) throws SQLException {
                try (PreparedStatement ps = connection.prepareStatement("DELETE FROM epics WHERE id=?")) {
                    ps.setInt(1, id);
                    ps.execute();

                    setResult(true);
                }
            }
        }.isResult();
    }

    @Override
    public Epic get(int id) {
        return new BaseSqlHelper<Epic>(connectionFactory) {
            @Override
            public void processing(Connection connection) throws SQLException {
                try (PreparedStatement ps = connection.prepareStatement("SELECT e.id, e.creation_date, e.name, e.priority, e.status, " +
                        "ex.id as ex_id, ex.email as ex_e, ex.name as ex_n, ex.password as ex_p, " +
                        "re.id as re_id, re.email as re_e, re.name as re_n, re.password as re_p, " +
                        "bl.id as bl_id, " +
                        "s.id as s_id, " +
                        "b.id as b_id, b.creation_date as b_creation, b.name as b_name, b.priority as b_prio, b.status as b_sts, " +
                        "str.id as str_id, str.creation_date as str_creation, str.name as str_name, str.priority as str_prio, str.status as str_sts, " +
                        "t.id as t_id, t.creation_date as t_creation, t.name as t_name, t.priority as t_prio, t.status as t_sts " +
                        "FROM epics e " +
                        "LEFT JOIN users ex ON e.executor_id = ex.id " +
                        "LEFT JOIN users re ON e.reporter_id = re.id " +
                        "LEFT JOIN backlog bl ON e.backlog_id = bl.id " +
                        "LEFT JOIN sprint s ON e.sprint_id = s.id " +
                        "LEFT JOIN bugs b ON e.root_bug_id = b.id " +
                        "LEFT JOIN stories str ON e.root_story_id = str.id " +
                        "LEFT JOIN tasks t ON e.root_task_id = t.id " +
                        "WHERE e.id=?")) {
                    ps.setInt(1, id);
                    ResultSet rs = ps.executeQuery();

                    if (!rs.next()) {
                        throw new NotExistException(String.format("Epic with ID:%d not exist.", id));
                    }

                    Epic epic = epicResultSetExtractor(rs);

                    setEntity(epic);
                }
            }
        }.getEntity();
    }

    @Override
    public Epic getByName(String name) {
        return new BaseSqlHelper<Epic>(connectionFactory) {
            @Override
            public void processing(Connection connection) throws SQLException {
                try (PreparedStatement ps = connection.prepareStatement("SELECT e.id, e.creation_date, e.name, e.priority, e.status, " +
                        "ex.id as ex_id, ex.email as ex_e, ex.name as ex_n, ex.password as ex_p, " +
                        "re.id as re_id, re.email as re_e, re.name as re_n, re.password as re_p, " +
                        "bl.id as bl_id, " +
                        "s.id as s_id, " +
                        "b.id as b_id, b.creation_date as b_creation, b.name as b_name, b.priority as b_prio, b.status as b_sts, " +
                        "str.id as str_id, str.creation_date as str_creation, str.name as str_name, str.priority as str_prio, str.status as str_sts, " +
                        "t.id as t_id, t.creation_date as t_creation, t.name as t_name, t.priority as t_prio, t.status as t_sts " +
                        "FROM epics e " +
                        "LEFT JOIN users ex ON e.executor_id = ex.id " +
                        "LEFT JOIN users re ON e.reporter_id = re.id " +
                        "LEFT JOIN backlog bl ON e.backlog_id = bl.id " +
                        "LEFT JOIN sprint s ON e.sprint_id = s.id " +
                        "LEFT JOIN bugs b ON e.root_bug_id = b.id " +
                        "LEFT JOIN stories str ON e.root_story_id = str.id " +
                        "LEFT JOIN tasks t ON e.root_task_id = t.id " +
                        "WHERE e.name=?")) {
                    ps.setString(1, name);
                    ResultSet rs = ps.executeQuery();

                    if (!rs.next()) {
                        throw new NotExistException(String.format("Epic with name:%s not exist.", name));
                    }

                    Epic epic = epicResultSetExtractor(rs);

                    setEntity(epic);
                }
            }
        }.getEntity();
    }

    @Override
    public List<Epic> getAll() {
        return new ArrayList<>(new BaseSqlHelper<Epic>(connectionFactory) {
            @Override
            public void processing(Connection connection) throws SQLException {
                try (PreparedStatement ps = connection.prepareStatement("SELECT e.id, e.creation_date, e.name, e.priority, e.status, " +
                        "ex.id as ex_id, ex.email as ex_e, ex.name as ex_n, ex.password as ex_p, " +
                        "re.id as re_id, re.email as re_e, re.name as re_n, re.password as re_p, " +
                        "bl.id as bl_id, " +
                        "s.id as s_id, " +
                        "b.id as b_id, b.creation_date as b_creation, b.name as b_name, b.priority as b_prio, b.status as b_sts, " +
                        "str.id as str_id, str.creation_date as str_creation, str.name as str_name, str.priority as str_prio, str.status as str_sts, " +
                        "t.id as t_id, t.creation_date as t_creation, t.name as t_name, t.priority as t_prio, t.status as t_sts " +
                        "FROM epics e " +
                        "LEFT JOIN users ex ON e.executor_id = ex.id " +
                        "LEFT JOIN users re ON e.reporter_id = re.id " +
                        "LEFT JOIN backlog bl ON e.backlog_id = bl.id " +
                        "LEFT JOIN sprint s ON e.sprint_id = s.id " +
                        "LEFT JOIN bugs b ON e.root_bug_id = e.id " +
                        "LEFT JOIN stories str ON b.root_story_id = str.id " +
                        "LEFT JOIN tasks t ON b.root_task_id = t.id " +
                        "ORDER BY e.creation_date")) {
                    ResultSet rs = ps.executeQuery();

                    Set<Epic> epics = new HashSet<>();

                    while (rs.next()) {
                        Epic epic = epicResultSetExtractor(rs);
                        epics.add(epic);
                    }

                    setEntities(epics);
                }
            }
        }.getEntities());
    }

    public void clear() {
        new BaseSqlHelper<Epic>(connectionFactory) {
            @Override
            public void processing(Connection connection) throws SQLException {
                try (PreparedStatement ps = connection.prepareStatement("DELETE FROM epics")){
                    ps.execute();
                }
            }
        };
    }

    private void setStatementEpic(PreparedStatement ps, Epic epic) throws SQLException {
        ps.setTimestamp(1, Timestamp.valueOf(epic.getCreationDate()));
        ps.setString(2, epic.getName());
        ps.setString(3, epic.getPriority().name());
        ps.setString(4, epic.getStatus().name());

        if (epic.getExecutor() != null && epic.getExecutor().getId() != null) {
            ps.setInt(5, epic.getExecutor().getId());
        } else {
            ps.setNull(5, Types.INTEGER);
        }

        if (epic.getReporter() != null && epic.getReporter().getId() != null) {
            ps.setInt(6, epic.getReporter().getId());
        } else {
            ps.setNull(6, Types.INTEGER);
        }

        if (epic.getBacklog() != null && epic.getBacklog().getId() != null) {
            ps.setInt(7, epic.getBacklog().getId());
        } else {
            ps.setNull(7, Types.INTEGER);
        }

        if (epic.getRootBug() != null && epic.getRootBug().getId() != null) {
            ps.setInt(8, epic.getRootBug().getId());
        } else {
            ps.setNull(8, Types.INTEGER);
        }

        if (epic.getRootStory() != null && epic.getRootStory().getId() != null) {
            ps.setInt(9, epic.getRootStory().getId());
        } else {
            ps.setNull(9, Types.INTEGER);
        }

        if (epic.getRootTask() != null && epic.getRootTask().getId() != null) {
            ps.setInt(10, epic.getRootTask().getId());
        } else {
            ps.setNull(10, Types.INTEGER);
        }

        if (epic.getSprint() != null && epic.getSprint().getId() != null) {
            ps.setInt(11, epic.getSprint().getId());
        } else {
            ps.setNull(11, Types.INTEGER);
        }
    }

    private Epic epicResultSetExtractor(ResultSet rs) throws SQLException {
        int id = rs.getInt("id");
        LocalDateTime creation_date = rs.getTimestamp("creation_date").toLocalDateTime();
        String name = rs.getString("name");
        String priority = rs.getString("priority");
        Priority epicPriority = getPriority(priority);
        WorkflowStatus status = getWorkflowStatus(rs.getString("status"));
        Epic epic = new Epic(epicPriority, creation_date, name, null, null, status);
        epic.setId(id);

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
            epic.setExecutor(executor);
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
            epic.setReporter(reporter);
        }

        int bl_id = rs.getInt("bl_id");
        if (bl_id != 0) {
            Backlog backlog = new Backlog();
            backlog.setId(bl_id);
            epic.setBacklog(backlog);
        }

        int s_id = rs.getInt("s_id");
        if (s_id != 0) {
            Sprint sprint = new Sprint();
            sprint.setId(s_id);
            epic.setSprint(sprint);
        }

        int b_id = rs.getInt("b_id");
        if (b_id != 0) {
            LocalDateTime bugCreation = rs.getTimestamp("b_creation").toLocalDateTime();
            String bugName = rs.getString("b_name");
            String bugPriorityString = rs.getString("b_prio");
            Priority bugPriority = getPriority(bugPriorityString);
            WorkflowStatus bugStatus = getWorkflowStatus(rs.getString("e_sts"));
            Bug rootBug = new Bug(bugPriority, bugCreation, bugName, null, null, bugStatus);
            rootBug.setId(b_id);
            epic.setRootBug(rootBug);
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
            epic.setRootStory(rootStory);
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
            epic.setRootTask(rootTask);
        }

        return epic;
    }
}
