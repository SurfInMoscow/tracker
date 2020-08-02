package ru.vorobyev.tracker.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.vorobyev.tracker.domain.issue.*;
import ru.vorobyev.tracker.domain.issue.workflow.WorkflowStatus;
import ru.vorobyev.tracker.domain.project.Backlog;
import ru.vorobyev.tracker.domain.project.Project;
import ru.vorobyev.tracker.domain.project.Sprint;
import ru.vorobyev.tracker.domain.user.User;
import ru.vorobyev.tracker.service.*;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

import static ru.vorobyev.tracker.utils.JdbcRepoUtils.getPriority;
import static ru.vorobyev.tracker.utils.JdbcRepoUtils.getWorkflowStatus;

@Controller
@RequestMapping("/issues")
public class IssuesController {

    private final ProjectService projectService;
    private final UserService userService;
    private final BacklogService backlogService;
    private final SprintService sprintService;
    private final IssueService<Bug> bugService;
    private final IssueService<Epic> epicService;
    private final IssueService<Story> storyService;
    private final IssueService<Task> taskService;

    @Autowired
    public IssuesController(ProjectService projectService, UserService userService, BacklogService backlogService,
                            SprintService sprintService, IssueService<Bug> bugService, IssueService<Epic> epicService, IssueService<Story> storyService,
                            IssueService<Task> taskService) {
        this.projectService = projectService;
        this.userService = userService;
        this.backlogService = backlogService;
        this.sprintService = sprintService;
        this.bugService = bugService;
        this.epicService = epicService;
        this.storyService = storyService;
        this.taskService = taskService;
    }

    @GetMapping
    protected String getIssues(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        req.setCharacterEncoding("UTF-8");
        int usr_id = (int) req.getAttribute("usr_id");
        String action = req.getParameter("action");
        String projectId = req.getParameter("project_id");

        User user = userService.get(usr_id);
        Project project = projectService.get(Integer.parseInt(projectId));

        Map<Integer, Project> userProjects = new HashMap<>();
        user.getProjects().forEach(p -> userProjects.put(p.getId(), p));

        if (userProjects.get(project.getId()) != null) {

            if (action == null) {
                return "redirect:/projects";
            }

            switch (action) {
                case "create":
                    return createIssue(req, user, project);
                case "edit":
                    return editIssue(req, user, project);
                case "delete":
                    return deleteIssue(req, project);
            }

        } else {
            req.setAttribute("exception", new ExceptionBean("Not allowed actions."));

            return "error";
        }

        return null;
    }

    @PostMapping
    protected String postIssues(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        int usr_id = (int) req.getAttribute("usr_id");
        String id = req.getParameter("id");

        User user = userService.get(usr_id);

        if (id == null) {
            return issueFromReq(req, resp, user, true);
        } else {
            return issueFromReq(req, resp, user, false);
        }
    }

    private String createIssue(HttpServletRequest req, User user, Project project) {
        req.setAttribute("backlog", project.getBacklog());
        req.setAttribute("project", project);
        req.setAttribute("user", user);

        return "createIssue";
    }

    private String issueFromReq(HttpServletRequest req, HttpServletResponse resp, User user, boolean isNew) throws ServletException, IOException {
        String backlogId = req.getParameter("backlog_id");
        String toSprint = req.getParameter("toSprint");
        String projectId = req.getParameter("project_id");
        String name = req.getParameter("name");
        String id = req.getParameter("id");
        String oldType = req.getParameter("old_type");
        String type = req.getParameter("type");
        String priority = req.getParameter("priority");
        String status = req.getParameter("status");
        String reporter = req.getParameter("reporter");
        String executor = req.getParameter("executor");

        Project project = projectService.get(Integer.parseInt(projectId));

        if (!user.getProjects().stream().collect(Collectors.groupingBy(Project::getId)).containsKey(project.getId())) {
            req.setAttribute("exception", new ExceptionBean("Not allowed actions."));

            return "error";
        }

        Backlog backlog = backlogService.get(Integer.parseInt(backlogId));
        Priority issuePriority = getPriority(priority);
        WorkflowStatus wkf = getWorkflowStatus(status);
        User reporterUser = reporter.equals(user.getEmail()) ? user : checkEmail(reporter, req, resp);
        User executorUser = executor.equals(user.getEmail()) ? user : checkEmail(executor, req, resp);

        boolean typeCheckOnEditWithOldValue = !oldType.equals(type) && !oldType.equals("mock");

        switch (type) {
            case "Bug":
                Bug bug = isNew ? new Bug(issuePriority, LocalDateTime.now(), name, executorUser, reporterUser, wkf)
                        : oldType.equals(type) ? bugService.get(Integer.parseInt(id))
                        : new Bug(issuePriority, LocalDateTime.now(), name, executorUser, reporterUser, wkf);

                if (toSprint != null && toSprint.equals("true")) {
                    bug.setSprint(project.getSprint());
                    bug.setBacklog(null);
                } else {
                    bug.setBacklog(backlog);
                }

                if (typeCheckOnEditWithOldValue) {
                    boolean deleteOld = oldType.equals("Epic") ? epicService.delete(Integer.parseInt(id))
                            : oldType.equals("Story") ? storyService.delete(Integer.parseInt(id))
                            : taskService.delete(Integer.parseInt(id));
                }

                if (bug.getId() != null) {
                    updateIssuesValues(bug, name, priority, status, reporter, executor);
                }

                bugService.save(bug);
                break;

            case "Epic":
                Epic epic = isNew ? new Epic(issuePriority, LocalDateTime.now(), name, executorUser, reporterUser, wkf)
                        : oldType.equals(type) ? epicService.get(Integer.parseInt(id))
                        : new Epic(issuePriority, LocalDateTime.now(), name, executorUser, reporterUser, wkf);

                if (toSprint != null && toSprint.equals("true")) {
                    epic.setSprint(project.getSprint());
                    epic.setBacklog(null);
                } else {
                    epic.setBacklog(backlog);
                }

                if (typeCheckOnEditWithOldValue) {
                    boolean deleteOld = oldType.equals("Bug") ? bugService.delete(Integer.parseInt(id))
                            : oldType.equals("Story") ? storyService.delete(Integer.parseInt(id))
                            : taskService.delete(Integer.parseInt(id));
                }

                if (epic.getId() != null) {
                    updateIssuesValues(epic, name, priority, status, reporter, executor);
                }

                epicService.save(epic);
                break;

            case "Story":
                Story story = isNew ? new Story(issuePriority, LocalDateTime.now(), name, executorUser, reporterUser, wkf)
                        : oldType.equals(type) ? storyService.get(Integer.parseInt(id))
                        : new Story(issuePriority, LocalDateTime.now(), name, executorUser, reporterUser, wkf);

                if (toSprint != null && toSprint.equals("true")) {
                    story.setSprint(project.getSprint());
                    story.setBacklog(null);
                } else {
                    story.setBacklog(backlog);
                }

                if (typeCheckOnEditWithOldValue) {
                    boolean deleteOld = oldType.equals("Bug") ? bugService.delete(Integer.parseInt(id))
                            : oldType.equals("Epic") ? epicService.delete(Integer.parseInt(id))
                            : taskService.delete(Integer.parseInt(id));
                }

                if (story.getId() != null) {
                    updateIssuesValues(story, name, priority, status, reporter, executor);
                }

                storyService.save(story);
                break;

            default:
                Task task = isNew ? new Task(issuePriority, LocalDateTime.now(), name, executorUser, reporterUser, wkf)
                        : oldType.equals(type) ? taskService.get(Integer.parseInt(id))
                        : new Task(issuePriority, LocalDateTime.now(), name, executorUser, reporterUser, wkf);

                if (toSprint != null && toSprint.equals("true")) {
                    task.setSprint(project.getSprint());
                    task.setBacklog(null);
                } else {
                    task.setBacklog(backlog);
                }

                if (typeCheckOnEditWithOldValue) {
                    boolean deleteOld = oldType.equals("Bug") ? bugService.delete(Integer.parseInt(id))
                            : oldType.equals("Epic") ? epicService.delete(Integer.parseInt(id))
                            : oldType.equals("Story") ? storyService.delete(Integer.parseInt(id))
                            : taskService.delete(Integer.parseInt(id));
                }

                if (task.getId() != null) {
                    updateIssuesValues(task, name, priority, status, reporter, executor);
                }

                taskService.save(task);
                break;
        }

        return "redirect:/projects?action=edit&id=" + projectId;
    }

    private String editIssue(HttpServletRequest req, User user, Project project) {
        String id = req.getParameter("id");
        String type = req.getParameter("type");

        switch (type) {
            case "Bug":
                Bug bug = bugService.get(Integer.parseInt(id));
                if (bug.getBacklog().getId().equals(project.getBacklog().getId()))
                    return editIssueForward(req, project, bug.getId(), bug.getName(),"Bug", bug.getPriority().name(), bug.getStatus().name(),
                            bug.getReporter().getEmail(), bug.getExecutor().getEmail());
                break;
            case "Epic":
                Epic epic = epicService.get(Integer.parseInt(id));
                if (epic.getBacklog().getId().equals(project.getBacklog().getId()))
                    return editIssueForward(req, project, epic.getId(), epic.getName(), "Epic", epic.getPriority().name(), epic.getStatus().name(),
                            epic.getReporter().getEmail(), epic.getExecutor().getEmail());
                break;
            case "Story":
                Story story = storyService.get(Integer.parseInt(id));
                if (story.getBacklog().getId().equals(project.getBacklog().getId()))
                    return editIssueForward(req, project, story.getId(), story.getName(), "Story", story.getPriority().name(), story.getStatus().name(),
                            story.getReporter().getEmail(), story.getExecutor().getEmail());
                break;
            case "Task":
                Task task = taskService.get(Integer.parseInt(id));
                if (task.getBacklog().getId().equals(project.getBacklog().getId()))
                    return editIssueForward(req, project, task.getId(), task.getName(), "Task", task.getPriority().name(), task.getStatus().name(),
                            task.getReporter().getEmail(), task.getExecutor().getEmail());
                break;
            case "Abstract":
                Sprint sprint = sprintService.get(project.getSprint().getId());
                if (sprint.getTasks().stream().anyMatch(i -> i.getId().equals(Integer.parseInt(id)))) {
                    Task taskAbstract = taskService.get(Integer.parseInt(id));
                    return editIssueForward(req, project, taskAbstract.getId(), taskAbstract.getName(), "Task", taskAbstract.getPriority().name(), taskAbstract.getStatus().name(),
                            taskAbstract.getReporter().getEmail(), taskAbstract.getExecutor().getEmail());
                }

                if (sprint.getStories().stream().anyMatch(i -> i.getId().equals(Integer.parseInt(id)))) {
                    Story storyAbstract = storyService.get(Integer.parseInt(id));
                    return editIssueForward(req, project, storyAbstract.getId(), storyAbstract.getName(), "Story", storyAbstract.getPriority().name(), storyAbstract.getStatus().name(),
                            storyAbstract.getReporter().getEmail(), storyAbstract.getExecutor().getEmail());
                }

                if (sprint.getBugs().stream().anyMatch(i -> i.getId().equals(Integer.parseInt(id)))) {
                    Bug bugAbstract = bugService.get(Integer.parseInt(id));
                    return editIssueForward(req, project, bugAbstract.getId(), bugAbstract.getName(), "Bug", bugAbstract.getPriority().name(), bugAbstract.getStatus().name(),
                            bugAbstract.getReporter().getEmail(), bugAbstract.getExecutor().getEmail());
                }

                if (sprint.getEpics().stream().anyMatch(i -> i.getId().equals(Integer.parseInt(id)))) {
                    Epic epicAbstract = epicService.get(Integer.parseInt(id));
                    return editIssueForward(req, project, epicAbstract.getId(), epicAbstract.getName(), "Epic", epicAbstract.getPriority().name(), epicAbstract.getStatus().name(),
                            epicAbstract.getReporter().getEmail(), epicAbstract.getExecutor().getEmail());
                }

                break;
        }

        return null;
    }

    private String editIssueForward(HttpServletRequest req, Project project, int id, String name, String type, String priority,
                                    String status, String reporter, String executor) {
        req.setAttribute("backlog", project.getBacklog());
        req.setAttribute("project", project);
        req.setAttribute("id", id);
        req.setAttribute("name", name);
        req.setAttribute("type", type);
        req.setAttribute("priority", priority);
        req.setAttribute("status", status);
        req.setAttribute("reporter", reporter);
        req.setAttribute("executor", executor);

        return "editIssue";
    }

    private String deleteIssue(HttpServletRequest req, Project project) {
        String id = req.getParameter("id");
        String type = req.getParameter("type");

        switch (type) {
            case "Bug":
                Bug bug = bugService.get(Integer.parseInt(id));
                if (bug.getBacklog().getId().equals(project.getBacklog().getId()))
                    bugService.delete(bug.getId());
                break;
            case "Epic":
                Epic epic = epicService.get(Integer.parseInt(id));
                if (epic.getBacklog().getId().equals(project.getBacklog().getId()))
                    epicService.delete(epic.getId());
                break;
            case "Story":
                Story story = storyService.get(Integer.parseInt(id));
                if (story.getBacklog().getId().equals(project.getBacklog().getId()))
                    storyService.delete(story.getId());
                break;
            case "Task":
                Task task = taskService.get(Integer.parseInt(id));
                if (task.getBacklog().getId().equals(project.getBacklog().getId()))
                    taskService.delete(Integer.parseInt(id));
                break;
            case "Abstract":
                Sprint sprint = sprintService.get(project.getSprint().getId());
                Set<AbstractIssue> issues = new HashSet<>();
                issues.addAll(sprint.getTasks());
                issues.addAll(sprint.getBugs());
                issues.addAll(sprint.getEpics());
                issues.addAll(sprint.getStories());
                if (issues.stream().anyMatch(i -> i.getId().equals(Integer.parseInt(id)))) {
                    bugService.delete(Integer.parseInt(id));
                    epicService.delete(Integer.parseInt(id));
                    storyService.delete(Integer.parseInt(id));
                    taskService.delete(Integer.parseInt(id));
                }
        }

        return "redirect:/projects?action=edit&id=" + project.getId();
    }

    private <T extends AbstractIssue> void updateIssuesValues(T issue, String name, String priority, String status, String reporter, String executor) {
        issue.setName(name);
        issue.setPriority(getPriority(priority));
        issue.setStatus(getWorkflowStatus(status));
        issue.setReporter(userService.getByEmail(reporter));
        issue.setExecutor(userService.getByEmail(executor));
    }

    private User checkEmail(String email, HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        User user = null;
        try {
            user = userService.getByEmail(email);
        } catch (Exception e) {
            req.setAttribute("exception", new ExceptionBean(String.format("User with email:%s not exist.", email)));
            req.getRequestDispatcher("/WEB-INF/templates/error.jsp").forward(req, resp);
        }

        return user;
    }
}
