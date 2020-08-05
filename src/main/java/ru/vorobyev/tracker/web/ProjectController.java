package ru.vorobyev.tracker.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.vorobyev.tracker.domain.issue.AbstractIssue;
import ru.vorobyev.tracker.domain.project.Backlog;
import ru.vorobyev.tracker.domain.project.Project;
import ru.vorobyev.tracker.domain.project.Sprint;
import ru.vorobyev.tracker.domain.user.User;
import ru.vorobyev.tracker.service.BacklogService;
import ru.vorobyev.tracker.service.ProjectService;
import ru.vorobyev.tracker.service.SprintService;
import ru.vorobyev.tracker.service.UserService;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

@Controller
@RequestMapping("/projects")
public class ProjectController {

    private final ProjectService projectService;
    private final UserService userService;
    private final BacklogService backlogService;
    private final SprintService sprintService;
    private final Environment env;

    @Autowired
    public ProjectController(ProjectService projectService, UserService userService, BacklogService backlogService, SprintService sprintService, Environment env) {
        this.projectService = projectService;
        this.userService = userService;
        this.backlogService = backlogService;
        this.sprintService = sprintService;
        this.env = env;
    }

    @GetMapping
    protected String getProjects(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        req.setCharacterEncoding("UTF-8");
        int usr_id = (int) req.getAttribute("usr_id");
        String id = req.getParameter("id");
        String action = req.getParameter("action");
        String userEmail = req.getParameter("user_email");
        User user = userService.get(usr_id);

        if (action == null) {
            req.setAttribute("user", user);
            req.setAttribute("projects", user.getProjects());

            return "projects";
        }

        switch (action) {
            case "delete":
                return deleteProject(id);
            case "dropuser":
                return dropUser(id, userEmail);
            case "edit":
                return editProject(req, id, user);
            case "logout":
                Arrays.asList(req.getCookies()).forEach(cookie -> {
                    cookie.setValue("");
                    cookie.setPath("/");
                    cookie.setMaxAge(0);
                    resp.addCookie(cookie);
                });

                return "redirect:/tracker";
        }

        return null;
    }

    @PostMapping
    protected String postProjects(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        int usr_id = (int) req.getAttribute("usr_id");
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

                return "redirect:/projects";
            }
        } else if (user_email != null) {
            return addParticipantToProject(req, resp, id, user_email);
        } else {
            return updateProject(req, resp, id, name, description, department, manager_email, admin_email, user);
        }

        return null;
    }

    private String updateProject(HttpServletRequest req, HttpServletResponse resp, String id, String name, String description, String department, String manager_email, String admin_email, User user) throws ServletException, IOException {
        Project project = projectService.get(Integer.parseInt(id));

        if (project.getAdministrator().equals(user.getEmail())) {
            if (checkEmail(manager_email, req, resp) && checkEmail(admin_email, req, resp)) {
                project.setName(name);
                project.setDescription(description);
                project.setDepartment(department);
                project.setManager(manager_email);
                project.setAdministrator(admin_email);
                project = projectService.save(project);

                return "redirect:/projects";
            }
        } else {
            return "error";
        }

        return null;
    }

    private String addParticipantToProject(HttpServletRequest req, HttpServletResponse resp, String id, String user_email) throws ServletException, IOException {
        if (checkEmail(user_email, req, resp)) {
            User participant = userService.getByEmail(user_email);
            Project project = projectService.get(Integer.parseInt(id));
            project.getParticipants().add(participant);
            projectService.save(project);

            return "redirect:/projects?action=edit&id=" + project.getId();
        }

        return null;
    }

    private String deleteProject(String id) {
        Project project = projectService.get(Integer.parseInt(id));
        projectService.delete(project.getId());

        if (Arrays.asList(env.getActiveProfiles()).contains("jpa")) {
            backlogService.delete(project.getBacklog().getId());
            sprintService.delete(project.getSprint().getId());
        }

        return "redirect:/projects";
    }

    private String dropUser(String id, String userEmail) {
        Project project = projectService.get(Integer.parseInt(id));
        User participant = userService.getByEmail(userEmail);
        project.getParticipants().remove(participant);
        projectService.save(project);

        return "redirect:/projects?action=edit&id=" + project.getId();
    }

    private String editProject(HttpServletRequest req, String id, User user) {
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

            return "editProject";
        }

        return null;
    }

    private void saveProject(String name, String description, String department, String manager_email, String admin_email, User user) {
        Backlog backlog = backlogService.save(new Backlog());
        Sprint sprint = sprintService.save(new Sprint());
        Project project = new Project(name, description, department, manager_email, user.getEmail());
        project.setBacklog(backlog);
        project.setSprint(sprint);
        project = projectService.save(project);
        User manager = userService.getByEmail(manager_email);
        User admin = userService.getByEmail(admin_email);
        project.getParticipants().add(user);
        project.getParticipants().add(admin);
        project.getParticipants().add(manager);
        projectService.save(project);
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
