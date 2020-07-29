package ru.vorobyev.tracker.web;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import ru.vorobyev.tracker.config.TrackerSpringConfig;
import ru.vorobyev.tracker.domain.issue.AbstractIssue;
import ru.vorobyev.tracker.domain.project.Backlog;
import ru.vorobyev.tracker.domain.project.Project;
import ru.vorobyev.tracker.domain.project.Sprint;
import ru.vorobyev.tracker.domain.user.User;
import ru.vorobyev.tracker.service.BacklogService;
import ru.vorobyev.tracker.service.ProjectService;
import ru.vorobyev.tracker.service.SprintService;
import ru.vorobyev.tracker.service.UserService;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class ProjectsServlet extends HttpServlet {

    private ProjectService projectService;
    private UserService userService;
    private BacklogService backlogService;
    private SprintService sprintService;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        AnnotationConfigApplicationContext apCtx = new AnnotationConfigApplicationContext(TrackerSpringConfig.class);
        projectService = apCtx.getBean(ProjectService.class);
        userService = apCtx.getBean(UserService.class);
        backlogService = apCtx.getBean(BacklogService.class);
        sprintService = apCtx.getBean(SprintService.class);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        int usr_id = extractCookieID(req);
        String id = req.getParameter("id");
        String action = req.getParameter("action");
        String userEmail = req.getParameter("user_email");
        User user = userService.get(usr_id);

        if (action == null) {
            req.setAttribute("user", user);
            req.setAttribute("projects", user.getProjects());
            req.getRequestDispatcher("/WEB-INF/templates/projects.jsp").forward(req, resp);
            return;
        }

        switch (action) {
            case "delete":
                deleteProject(resp, id);
                return;
            case "dropuser":
                dropUser(resp, id, userEmail);
                return;
            case "edit":
                editProject(req, resp, id, user);
                return;
            case "logout":
                Arrays.asList(req.getCookies()).forEach(cookie -> {
                    cookie.setValue("");
                    cookie.setPath("/");
                    cookie.setMaxAge(0);
                    resp.addCookie(cookie);
                });
                resp.sendRedirect("tracker");
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        int usr_id = extractCookieID(req);
        String id = req.getParameter("id");
        String name = req.getParameter("name");
        String description = req.getParameter("description");
        String department = req.getParameter("department");
        String manager_email = req.getParameter("manager_email");
        String admin_email = req.getParameter("admin_email");
        String user_email = req.getParameter("user_email");
        User user = userService.get(usr_id);

        if (id == null) {
            if (checkEmail(manager_email, req, resp) && checkEmail(admin_email, req, resp)) {
                saveProject(name, description, department, manager_email, admin_email, user);
                resp.sendRedirect("projects");
            }
        } else if (user_email != null) {
            addParticipantToProject(req, resp, id, user_email);
        } else {
            updateProject(req, resp, id, name, description, department, manager_email, admin_email, user);
        }
    }

    private int extractCookieID(HttpServletRequest req) {
        return Integer.parseInt(Arrays.stream(req.getCookies()).filter(cookie -> cookie.getName().equals("usr_id")).findFirst().get().getValue());
    }

    private void updateProject(HttpServletRequest req, HttpServletResponse resp, String id, String name, String description, String department, String manager_email, String admin_email, User user) throws ServletException, IOException {
        Project project = projectService.get(Integer.parseInt(id));

        if (project.getAdministrator().equals(user.getEmail())) {
            if (checkEmail(manager_email, req, resp) && checkEmail(admin_email, req, resp)) {
                project.setName(name);
                project.setDescription(description);
                project.setDepartment(department);
                project.setManager(manager_email);
                project.setAdministrator(admin_email);
                project = projectService.save(project);
                resp.sendRedirect("projects");
            }
        } else {
            req.getRequestDispatcher("/WEB-INF/templates/error.jsp").forward(req, resp);
        }
    }

    private void addParticipantToProject(HttpServletRequest req, HttpServletResponse resp, String id, String user_email) throws ServletException, IOException {
        if (checkEmail(user_email, req, resp)) {
            User participant = userService.getByEmail(user_email);
            Project project = projectService.get(Integer.parseInt(id));
            project.getParticipants().add(participant);
            projectService.save(project);
            resp.sendRedirect("projects?action=edit&id=" + project.getId());
        }
    }

    private void deleteProject(HttpServletResponse resp, String id) throws IOException {
        Project project = projectService.get(Integer.parseInt(id));
        backlogService.delete(project.getBacklog().getId());
        sprintService.delete(project.getSprint().getId());
        projectService.delete(project.getId());
        resp.sendRedirect("projects");
    }

    private void dropUser(HttpServletResponse resp, String id, String userEmail) throws IOException {
        Project project = projectService.get(Integer.parseInt(id));
        User participant = userService.getByEmail(userEmail);
        project.getParticipants().remove(participant);
        projectService.save(project);
        resp.sendRedirect("projects?action=edit&id=" + project.getId());
    }

    private void editProject(HttpServletRequest req, HttpServletResponse resp, String id, User user) throws ServletException, IOException {
        Project project = projectService.get(Integer.parseInt(id));
        Backlog backlog = backlogService.get(project.getBacklog().getId());
        Sprint sprint = sprintService.get(project.getSprint().getId());
        Set<AbstractIssue> issues = new HashSet<>();
        issues.addAll(sprint.getTasks());
        issues.addAll(sprint.getStories());
        issues.addAll(sprint.getBugs());
        issues.addAll(sprint.getEpics());
        if (securityCheck(user, project)) {
            req.setAttribute("user", user);
            req.setAttribute("project", project);
            req.setAttribute("bugs", backlog.getBugs());
            req.setAttribute("epics", backlog.getEpics());
            req.setAttribute("stories", backlog.getStories());
            req.setAttribute("tasks", backlog.getTasks());
            req.setAttribute("sprints", issues);
            req.setAttribute("users", project.getParticipants());
            req.getRequestDispatcher("/WEB-INF/templates/editProject.jsp").forward(req, resp);
        }
    }

    private void saveProject(String name, String description, String department, String manager_email, String admin_email, User user) {
        Backlog backlog = backlogService.save(new Backlog());
        Sprint sprint = sprintService.save(new Sprint());
        Project project = new Project(name, description, department, manager_email, user.getEmail());
        project.setBacklog(backlog);
        project.setSprint(sprint);
        project = projectService.save(project);
        user.getProjects().add(project);
        user = userService.save(user);
        User manager = userService.getByEmail(manager_email);
        manager.getProjects().add(project);
        User admin = userService.getByEmail(admin_email);
        admin.getProjects().add(project);
        userService.save(manager);
        userService.save(admin);
    }

    private boolean securityCheck(User user, Project project) {
        for (Project p : user.getProjects()) {
            if (p.getId().equals(project.getId()))
                return true;
        }

        return false;
    }

    private boolean checkEmail(String email, HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        boolean check = true;
        User user;
        try {
            user = userService.getByEmail(email);
        } catch (Exception e) {
            req.setAttribute("exception", new ExceptionBean(String.format("User with email:%s not exist.", email)));
            req.getRequestDispatcher("/WEB-INF/templates/error.jsp").forward(req, resp);
            check = false;
        }

        return check;
    }
}
