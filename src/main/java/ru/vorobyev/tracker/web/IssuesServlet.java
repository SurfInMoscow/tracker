package ru.vorobyev.tracker.web;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import ru.vorobyev.tracker.config.TrackerConfig;
import ru.vorobyev.tracker.domain.project.Project;
import ru.vorobyev.tracker.domain.user.User;
import ru.vorobyev.tracker.service.ProjectService;
import ru.vorobyev.tracker.service.UserService;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class IssuesServlet extends HttpServlet {

    private ProjectService projectService;
    private UserService userService;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        AnnotationConfigApplicationContext apCtx = new AnnotationConfigApplicationContext(TrackerConfig.class);
        projectService = apCtx.getBean(ProjectService.class);
        userService = apCtx.getBean(UserService.class);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        HttpSession session = req.getSession();
        int usr_id = (int) session.getAttribute("usr_id");
        String id = req.getParameter("id");
        String action = req.getParameter("action");

        User user = userService.get(usr_id);
        Project project = projectService.get(Integer.parseInt(id));

        Map<Integer, Project> userProjects = new HashMap<>();
        user.getProjects().forEach(p -> userProjects.put(p.getId(), p));

        if (userProjects.get(project.getId()) != null) {

            if (action == null) {
                req.setAttribute("backlog", project.getBacklog());
                req.setAttribute("sprint", project.getSprint());
                req.getRequestDispatcher("/WEB-INF/templates/projects.jsp").forward(req, resp);
                return;
            }

            switch (action) {
                //TODO
            }

        } else {
            req.getRequestDispatcher("/WEB-INF/templates/error.jsp").forward(req, resp);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        //TODO
    }
}
