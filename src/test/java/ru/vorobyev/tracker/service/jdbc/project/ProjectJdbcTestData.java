package ru.vorobyev.tracker.service.jdbc.project;

import ru.vorobyev.tracker.domain.project.Backlog;
import ru.vorobyev.tracker.domain.project.Project;
import ru.vorobyev.tracker.domain.project.Sprint;

import java.util.HashSet;

public class ProjectJdbcTestData {
   public static final Backlog BACKLOG1 = new Backlog(new HashSet<>(), new HashSet<>(), new HashSet<>(), new HashSet<>());
   public static final Backlog BACKLOG2 = new Backlog(new HashSet<>(), new HashSet<>(), new HashSet<>(), new HashSet<>());
   public static final Backlog BACKLOG3 = new Backlog(new HashSet<>(), new HashSet<>(), new HashSet<>(), new HashSet<>());

   public static final Project PROJECT1 = new Project("Project1", "unit-test project", "java department", "Manager", "Admin");
   public static final Project PROJECT2 = new Project("Project2", "unit-test project", "red hat", "Super Manager", "Super Admin");
   public static final Project PROJECT3 = new Project("Project3", "unit-test project", "oracle", "Regular Manager", "Regular Admin");

   public static final Sprint SPRINT1 = new Sprint();
   public static final Sprint SPRINT2 = new Sprint();
   public static final Sprint SPRINT3 = new Sprint();
}
