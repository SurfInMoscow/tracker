package ru.vorobyev.tracker.repository.jdbc.issue;

import org.springframework.beans.factory.annotation.Qualifier;
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
@Qualifier("StoryRepository")
public class StoryJdbcRepositoryImpl implements IssueRepository<Story> {

    private final ConnectionFactory connectionFactory;

    public StoryJdbcRepositoryImpl() {
        this.connectionFactory = () -> DatabaseConnection.getInstance().getConnection();
    }

    @Override
    public Story save(Story story) {
        if (story.isNew()) {
            return new BaseSqlHelper<Story>(connectionFactory) {
                @Override
                public void processing(Connection connection) throws SQLException {
                    try (PreparedStatement ps = connection.prepareStatement("INSERT INTO stories(creation_date, name, priority, status, executor_id, reporter_id, " +
                                    "backlog_id, root_bug_id, root_epic_id, root_task_id, sprint_id) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)",
                            Statement.RETURN_GENERATED_KEYS)) {
                        setStatementStory(ps, story);

                        ps.executeUpdate();

                        try (ResultSet generatedKeys = ps.getGeneratedKeys()) {
                            if (generatedKeys.next()) {
                                story.setId(generatedKeys.getInt(1));
                            } else {
                                throw new SQLException("No ID generated.");
                            }
                        }

                        setEntity(story);
                    }
                }
            }.getEntity();
        } else {
            return new BaseSqlHelper<Story>(connectionFactory) {
                @Override
                public void processing(Connection connection) throws SQLException {
                    try (PreparedStatement ps = connection.prepareStatement("UPDATE stories SET creation_date=?, name=?, priority=?, status=?, executor_id=?, reporter_id=?, " +
                            "backlog_id=?, root_bug_id=?, root_epic_id=?, root_task_id=?, sprint_id=? WHERE id=?")) {
                        setStatementStory(ps, story);
                        ps.setInt(12, story.getId());

                        ps.executeUpdate();

                        setEntity(story);
                    }
                }
            }.getEntity();
        }
    }

    @Override
    public boolean delete(int id) {
        return new BaseSqlHelper<Story>(connectionFactory) {
            @Override
            public void processing(Connection connection) throws SQLException {
                try (PreparedStatement ps = connection.prepareStatement("DELETE FROM stories WHERE id=?")) {
                    ps.setInt(1, id);
                    ps.execute();

                    setResult(true);
                }
            }
        }.isResult();
    }

    @Override
    public Story get(int id) {
        return new BaseSqlHelper<Story>(connectionFactory) {
            @Override
            public void processing(Connection connection) throws SQLException {
                try (PreparedStatement ps = connection.prepareStatement("SELECT str.id, str.creation_date, str.name, str.priority, str.status, " +
                        "ex.id as ex_id, ex.email as ex_e, ex.name as ex_n, ex.password as ex_p, " +
                        "re.id as re_id, re.email as re_e, re.name as re_n, re.password as re_p, " +
                        "bl.id as bl_id, " +
                        "s.id as s_id, " +
                        "b.id as b_id, b.creation_date as b_creation, b.name as b_name, b.priority as b_prio, b.status as b_sts, " +
                        "e.id as e_id, e.creation_date as e_creation, e.name as e_name, e.priority as e_prio, e.status as e_sts, " +
                        "t.id as t_id, t.creation_date as t_creation, t.name as t_name, t.priority as t_prio, t.status as t_sts " +
                        "FROM stories str " +
                        "LEFT JOIN users ex ON str.executor_id = ex.id " +
                        "LEFT JOIN users re ON str.reporter_id = re.id " +
                        "LEFT JOIN backlog bl ON str.backlog_id = bl.id " +
                        "LEFT JOIN sprint s ON str.sprint_id = s.id " +
                        "LEFT JOIN bugs b ON str.root_bug_id = b.id " +
                        "LEFT JOIN epics e ON str.root_epic_id = e.id " +
                        "LEFT JOIN tasks t ON str.root_task_id = t.id " +
                        "WHERE str.id=?")) {
                    ps.setInt(1, id);
                    ResultSet rs = ps.executeQuery();

                    if (!rs.next()) {
                        throw new NotExistException(String.format("Story with ID:%d not exist.", id));
                    }

                    Story story = storyResultSetExtractor(rs);

                    setEntity(story);
                }
            }
        }.getEntity();
    }

    @Override
    public Story getByName(String name) {
        return new BaseSqlHelper<Story>(connectionFactory) {
            @Override
            public void processing(Connection connection) throws SQLException {
                try (PreparedStatement ps = connection.prepareStatement("SELECT str.id, str.creation_date, str.name, str.priority, str.status, " +
                        "ex.id as ex_id, ex.email as ex_e, ex.name as ex_n, ex.password as ex_p, " +
                        "re.id as re_id, re.email as re_e, re.name as re_n, re.password as re_p, " +
                        "bl.id as bl_id, " +
                        "s.id as s_id, " +
                        "b.id as b_id, b.creation_date as b_creation, b.name as b_name, b.priority as b_prio, b.status as b_sts, " +
                        "e.id as e_id, e.creation_date as e_creation, e.name as e_name, e.priority as e_prio, e.status as e_sts, " +
                        "t.id as t_id, t.creation_date as t_creation, t.name as t_name, t.priority as t_prio, t.status as t_sts " +
                        "FROM stories str " +
                        "LEFT JOIN users ex ON str.executor_id = ex.id " +
                        "LEFT JOIN users re ON str.reporter_id = re.id " +
                        "LEFT JOIN backlog bl ON str.backlog_id = bl.id " +
                        "LEFT JOIN sprint s ON str.sprint_id = s.id " +
                        "LEFT JOIN bugs b ON str.root_bug_id = b.id " +
                        "LEFT JOIN epics e ON str.root_epic_id = e.id " +
                        "LEFT JOIN tasks t ON str.root_task_id = t.id " +
                        "WHERE str.name=?")) {
                    ps.setString(1, name);
                    ResultSet rs = ps.executeQuery();

                    if (!rs.next()) {
                        throw new NotExistException(String.format("Story with name:%s not exist.", name));
                    }

                    Story story = storyResultSetExtractor(rs);

                    setEntity(story);
                }
            }
        }.getEntity();
    }

    @Override
    public List<Story> getAll() {
        return new ArrayList<>(new BaseSqlHelper<Story>(connectionFactory) {
            @Override
            public void processing(Connection connection) throws SQLException {
                try (PreparedStatement ps = connection.prepareStatement("SELECT str.id, str.creation_date, str.name, str.priority, str.status, " +
                        "ex.id as ex_id, ex.email as ex_e, ex.name as ex_n, ex.password as ex_p, " +
                        "re.id as re_id, re.email as re_e, re.name as re_n, re.password as re_p, " +
                        "bl.id as bl_id, " +
                        "s.id as s_id, " +
                        "b.id as b_id, b.creation_date as b_creation, b.name as b_name, b.priority as b_prio, b.status as b_sts, " +
                        "e.id as e_id, e.creation_date as e_creation, e.name as e_name, e.priority as e_prio, e.status as e_sts, " +
                        "t.id as t_id, t.creation_date as t_creation, t.name as t_name, t.priority as t_prio, t.status as t_sts " +
                        "FROM stories str " +
                        "LEFT JOIN users ex ON str.executor_id = ex.id " +
                        "LEFT JOIN users re ON str.reporter_id = re.id " +
                        "LEFT JOIN backlog bl ON str.backlog_id = bl.id " +
                        "LEFT JOIN sprint s ON str.sprint_id = s.id " +
                        "LEFT JOIN bugs b ON str.root_bug_id = b.id " +
                        "LEFT JOIN epics e ON str.root_epic_id = e.id " +
                        "LEFT JOIN tasks t ON str.root_task_id = t.id " +
                        "ORDER BY str.creation_date")) {
                    ResultSet rs = ps.executeQuery();

                    Set<Story> stories = new HashSet<>();

                    while (rs.next()) {
                        Story story = storyResultSetExtractor(rs);
                        stories.add(story);
                    }

                    setEntities(stories);
                }
            }
        }.getEntities());
    }

    public void clear() {
        new BaseSqlHelper<Story>(connectionFactory) {
            @Override
            public void processing(Connection connection) throws SQLException {
                try (PreparedStatement ps = connection.prepareStatement("DELETE FROM stories")){
                    ps.execute();
                }
            }
        };
    }

    private void setStatementStory(PreparedStatement ps, Story story) throws SQLException {
        ps.setTimestamp(1, Timestamp.valueOf(story.getCreationDate()));
        ps.setString(2, story.getName());
        ps.setString(3, story.getPriority().name());
        ps.setString(4, story.getStatus().name());

        if (story.getExecutor() != null && story.getExecutor().getId() != null) {
            ps.setInt(5, story.getExecutor().getId());
        } else {
            ps.setNull(5, Types.INTEGER);
        }

        if (story.getReporter() != null && story.getReporter().getId() != null) {
            ps.setInt(6, story.getReporter().getId());
        } else {
            ps.setNull(6, Types.INTEGER);
        }

        if (story.getBacklog() != null && story.getBacklog().getId() != null) {
            ps.setInt(7, story.getBacklog().getId());
        } else {
            ps.setNull(7, Types.INTEGER);
        }

        if (story.getRootBug() != null && story.getRootBug().getId() != null) {
            ps.setInt(8, story.getRootBug().getId());
        } else {
            ps.setNull(8, Types.INTEGER);
        }

        if (story.getRootEpic() != null && story.getRootEpic().getId() != null) {
            ps.setInt(9, story.getRootEpic().getId());
        } else {
            ps.setNull(9, Types.INTEGER);
        }

        if (story.getRootTask() != null && story.getRootTask().getId() != null) {
            ps.setInt(10, story.getRootTask().getId());
        } else {
            ps.setNull(10, Types.INTEGER);
        }

        if (story.getSprint() != null && story.getSprint().getId() != null) {
            ps.setInt(11, story.getSprint().getId());
        } else {
            ps.setNull(11, Types.INTEGER);
        }
    }

    private Story storyResultSetExtractor(ResultSet rs) throws SQLException {
        int id = rs.getInt("id");
        LocalDateTime creation_date = rs.getTimestamp("creation_date").toLocalDateTime();
        String name = rs.getString("name");
        String priority = rs.getString("priority");
        Priority storyPriority = getPriority(priority);
        WorkflowStatus status = getWorkflowStatus(rs.getString("status"));
        Story story = new Story(storyPriority, creation_date, name, null, null, status);
        story.setId(id);

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
            story.setExecutor(executor);
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
            story.setReporter(reporter);
        }

        int bl_id = rs.getInt("bl_id");
        if (bl_id != 0) {
            Backlog backlog = new Backlog();
            backlog.setId(bl_id);
            story.setBacklog(backlog);
        }

        int s_id = rs.getInt("s_id");
        if (s_id != 0) {
            Sprint sprint = new Sprint();
            sprint.setId(s_id);
            story.setSprint(sprint);
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
            story.setRootBug(rootBug);
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
            story.setRootEpic(rootEpic);
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
            story.setRootTask(rootTask);
        }

        return story;
    }
}
