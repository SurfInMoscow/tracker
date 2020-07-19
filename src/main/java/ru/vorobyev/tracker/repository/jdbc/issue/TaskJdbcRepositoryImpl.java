package ru.vorobyev.tracker.repository.jdbc.issue;

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

public class TaskJdbcRepositoryImpl implements IssueRepository<Task> {

    private final ConnectionFactory connectionFactory;

    public TaskJdbcRepositoryImpl() {
        this.connectionFactory = () -> DatabaseConnection.getInstance().getConnection();
    }

    @Override
    public Task save(Task task) {
        if (task.isNew()) {
            return new BaseSqlHelper<Task>(connectionFactory) {
                @Override
                public void processing(Connection connection) throws SQLException {
                    try (PreparedStatement ps = connection.prepareStatement("INSERT INTO tasks(creation_date, name, priority, status, executor_id, reporter_id, " +
                                    "backlog_id, root_bug_id, root_epic_id, root_story_id, sprint_id) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)",
                            Statement.RETURN_GENERATED_KEYS)) {
                        setStatementTask(ps, task);

                        ps.executeUpdate();

                        try (ResultSet generatedKeys = ps.getGeneratedKeys()) {
                            if (generatedKeys.next()) {
                                task.setId(generatedKeys.getInt(1));
                            } else {
                                throw new SQLException("No ID generated.");
                            }
                        }

                        setEntity(task);
                    }
                }
            }.getEntity();
        } else {
            return new BaseSqlHelper<Task>(connectionFactory) {
                @Override
                public void processing(Connection connection) throws SQLException {
                    try (PreparedStatement ps = connection.prepareStatement("UPDATE tasks SET creation_date=?, name=?, priority=?, status=?, executor_id=?, reporter_id=?, " +
                            "backlog_id=?, root_bug_id=?, root_epic_id=?, root_story_id=?, sprint_id=? WHERE id=?")) {
                        setStatementTask(ps, task);
                        ps.setInt(12, task.getId());

                        ps.executeUpdate();

                        setEntity(task);
                    }
                }
            }.getEntity();
        }
    }

    @Override
    public boolean delete(int id) {
        return new BaseSqlHelper<Task>(connectionFactory) {
            @Override
            public void processing(Connection connection) throws SQLException {
                try (PreparedStatement ps = connection.prepareStatement("DELETE FROM tasks WHERE id=?")) {
                    ps.setInt(1, id);
                    ps.execute();

                    setResult(true);
                }
            }
        }.isResult();
    }

    @Override
    public Task get(int id) {
        return new BaseSqlHelper<Task>(connectionFactory) {
            @Override
            public void processing(Connection connection) throws SQLException {
                try (PreparedStatement ps = connection.prepareStatement("SELECT t.id, t.creation_date, t.name, t.priority, t.status, " +
                        "ex.id as ex_id, ex.email as ex_e, ex.name as ex_n, ex.password as ex_p, " +
                        "re.id as re_id, re.email as re_e, re.name as re_n, re.password as re_p, " +
                        "bl.id as bl_id, " +
                        "s.id as s_id, " +
                        "b.id as b_id, b.creation_date as b_creation, b.name as b_name, b.priority as b_prio, b.status as b_sts, " +
                        "e.id as e_id, e.creation_date as e_creation, e.name as e_name, e.priority as e_prio, e.status as e_sts, " +
                        "str.id as str_id, str.creation_date as str_creation, str.name as str_name, str.priority as str_prio, str.status as str_sts " +
                        "FROM tasks t " +
                        "LEFT JOIN users ex ON t.executor_id = ex.id " +
                        "LEFT JOIN users re ON t.reporter_id = re.id " +
                        "LEFT JOIN backlog bl ON t.backlog_id = bl.id " +
                        "LEFT JOIN sprint s ON t.sprint_id = s.id " +
                        "LEFT JOIN bugs b ON t.root_bug_id = b.id " +
                        "LEFT JOIN epics e ON t.root_epic_id = e.id " +
                        "LEFT JOIN stories str ON t.root_story_id = str.id " +
                        "WHERE t.id=?")) {
                    ps.setInt(1, id);
                    ResultSet rs = ps.executeQuery();

                    if (!rs.next()) {
                        throw new NotExistException(String.format("Task with ID:%d not exist.", id));
                    }

                    Task task = taskResultSetExtractor(rs);

                    setEntity(task);
                }
            }
        }.getEntity();
    }

    @Override
    public Task getByName(String name) {
        return new BaseSqlHelper<Task>(connectionFactory) {
            @Override
            public void processing(Connection connection) throws SQLException {
                try (PreparedStatement ps = connection.prepareStatement("SELECT t.id, t.creation_date, t.name, t.priority, t.status, " +
                        "ex.id as ex_id, ex.email as ex_e, ex.name as ex_n, ex.password as ex_p, " +
                        "re.id as re_id, re.email as re_e, re.name as re_n, re.password as re_p, " +
                        "bl.id as bl_id, " +
                        "s.id as s_id, " +
                        "b.id as b_id, b.creation_date as b_creation, b.name as b_name, b.priority as b_prio, b.status as b_sts, " +
                        "e.id as e_id, e.creation_date as e_creation, e.name as e_name, e.priority as e_prio, e.status as e_sts, " +
                        "str.id as str_id, str.creation_date as str_creation, str.name as str_name, str.priority as str_prio, str.status as str_sts " +
                        "FROM tasks t " +
                        "LEFT JOIN users ex ON t.executor_id = ex.id " +
                        "LEFT JOIN users re ON t.reporter_id = re.id " +
                        "LEFT JOIN backlog bl ON t.backlog_id = bl.id " +
                        "LEFT JOIN sprint s ON t.sprint_id = s.id " +
                        "LEFT JOIN bugs b ON t.root_bug_id = b.id " +
                        "LEFT JOIN epics e ON t.root_epic_id = e.id " +
                        "LEFT JOIN stories str ON t.root_story_id = str.id " +
                        "WHERE t.name=?")) {
                    ps.setString(1, name);
                    ResultSet rs = ps.executeQuery();

                    if (!rs.next()) {
                        throw new NotExistException(String.format("Task with name:%s not exist.", name));
                    }

                    Task task = taskResultSetExtractor(rs);

                    setEntity(task);
                }
            }
        }.getEntity();
    }

    @Override
    public List<Task> getAll() {
        return new ArrayList<>(new BaseSqlHelper<Task>(connectionFactory) {
            @Override
            public void processing(Connection connection) throws SQLException {
                try (PreparedStatement ps = connection.prepareStatement("SELECT t.id, t.creation_date, t.name, t.priority, t.status, " +
                        "ex.id as ex_id, ex.email as ex_e, ex.name as ex_n, ex.password as ex_p, " +
                        "re.id as re_id, re.email as re_e, re.name as re_n, re.password as re_p, " +
                        "bl.id as bl_id, " +
                        "s.id as s_id, " +
                        "b.id as b_id, b.creation_date as b_creation, b.name as b_name, b.priority as b_prio, b.status as b_sts, " +
                        "e.id as e_id, e.creation_date as e_creation, e.name as e_name, e.priority as e_prio, e.status as e_sts, " +
                        "str.id as str_id, str.creation_date as str_creation, str.name as str_name, str.priority as str_prio, str.status as str_sts " +
                        "FROM tasks t " +
                        "LEFT JOIN users ex ON t.executor_id = ex.id " +
                        "LEFT JOIN users re ON t.reporter_id = re.id " +
                        "LEFT JOIN backlog bl ON t.backlog_id = bl.id " +
                        "LEFT JOIN sprint s ON t.sprint_id = s.id " +
                        "LEFT JOIN bugs b ON t.root_bug_id = b.id " +
                        "LEFT JOIN epics e ON t.root_epic_id = e.id " +
                        "LEFT JOIN stories str ON t.root_story_id = str.id " +
                        "ORDER BY t.creation_date")) {
                    ResultSet rs = ps.executeQuery();

                    Set<Task> tasks = new HashSet<>();

                    while (rs.next()) {
                        Task task = taskResultSetExtractor(rs);
                        tasks.add(task);
                    }

                    setEntities(tasks);
                }
            }
        }.getEntities());
    }

    public void clear() {
        new BaseSqlHelper<Task>(connectionFactory) {
            @Override
            public void processing(Connection connection) throws SQLException {
                try (PreparedStatement ps = connection.prepareStatement("DELETE FROM tasks")){
                    ps.execute();
                }
            }
        };
    }

    private void setStatementTask(PreparedStatement ps, Task task) throws SQLException {
        ps.setTimestamp(1, Timestamp.valueOf(task.getCreationDate()));
        ps.setString(2, task.getName());
        ps.setString(3, task.getPriority().name());
        ps.setString(4, task.getStatus().name());

        if (task.getExecutor() != null && task.getExecutor().getId() != null) {
            ps.setInt(5, task.getExecutor().getId());
        } else {
            ps.setNull(5, Types.INTEGER);
        }

        if (task.getReporter() != null && task.getReporter().getId() != null) {
            ps.setInt(6, task.getReporter().getId());
        } else {
            ps.setNull(6, Types.INTEGER);
        }

        if (task.getBacklog() != null && task.getBacklog().getId() != null) {
            ps.setInt(7, task.getBacklog().getId());
        } else {
            ps.setNull(7, Types.INTEGER);
        }

        if (task.getRootBug() != null && task.getRootBug().getId() != null) {
            ps.setInt(8, task.getRootBug().getId());
        } else {
            ps.setNull(8, Types.INTEGER);
        }

        if (task.getRootEpic() != null && task.getRootEpic().getId() != null) {
            ps.setInt(9, task.getRootEpic().getId());
        } else {
            ps.setNull(9, Types.INTEGER);
        }

        if (task.getRootStory() != null && task.getRootStory().getId() != null) {
            ps.setInt(10, task.getRootStory().getId());
        } else {
            ps.setNull(10, Types.INTEGER);
        }

        if (task.getSprint() != null && task.getSprint().getId() != null) {
            ps.setInt(11, task.getSprint().getId());
        } else {
            ps.setNull(11, Types.INTEGER);
        }
    }

    private Task taskResultSetExtractor(ResultSet rs) throws SQLException {
        int id = rs.getInt("id");
        LocalDateTime creation_date = rs.getTimestamp("creation_date").toLocalDateTime();
        String name = rs.getString("name");
        String priority = rs.getString("priority");
        Priority taskPriority = getPriority(priority);
        WorkflowStatus status = getWorkflowStatus(rs.getString("status"));
        Task task = new Task(taskPriority, creation_date, name, null, null, status);
        task.setId(id);

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
            task.setExecutor(executor);
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
            task.setReporter(reporter);
        }

        int bl_id = rs.getInt("bl_id");
        if (bl_id != 0) {
            Backlog backlog = new Backlog();
            backlog.setId(bl_id);
            task.setBacklog(backlog);
        }

        int s_id = rs.getInt("s_id");
        if (s_id != 0) {
            Sprint sprint = new Sprint();
            sprint.setId(s_id);
            task.setSprint(sprint);
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
            task.setRootBug(rootBug);
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
            task.setRootEpic(rootEpic);
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
            task.setRootStory(rootStory);
        }

        return task;
    }
}
