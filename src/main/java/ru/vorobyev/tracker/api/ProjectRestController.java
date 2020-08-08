package ru.vorobyev.tracker.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import ru.vorobyev.tracker.domain.project.Project;
import ru.vorobyev.tracker.exception.IllegalRequestException;
import ru.vorobyev.tracker.service.BacklogService;
import ru.vorobyev.tracker.service.ProjectService;
import ru.vorobyev.tracker.service.SprintService;
import ru.vorobyev.tracker.to.project.ProjectTo;

import java.net.URI;
import java.util.Objects;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping(value = ProjectRestController.REST_URL, produces = APPLICATION_JSON_VALUE)
public class ProjectRestController {

    protected final static String REST_URL = "/api/projects";

    private final ProjectService projectService;
    private final BacklogService backlogService;
    private final SprintService sprintService;

    @Autowired
    public ProjectRestController(ProjectService projectService, BacklogService backlogService, SprintService sprintService) {
        this.projectService = projectService;
        this.backlogService = backlogService;
        this.sprintService = sprintService;
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_USER')")
    @GetMapping("/{id}")
    public ProjectTo getProject(@PathVariable int id) {
        return new ProjectTo(projectService.get(id));
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_USER')")
    @DeleteMapping("/{id}")
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void deleteProject(@PathVariable int id) {
        Project project = projectService.get(id);
        projectService.delete(id);
        backlogService.delete(project.getBacklog().getId());
        sprintService.delete(project.getSprint().getId());
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_USER')")
    @PostMapping(consumes = APPLICATION_JSON_VALUE)
    public ResponseEntity<ProjectTo> saveProject(@RequestBody ProjectTo projectTo) {
        Objects.requireNonNull(projectTo, "Bad request.");
        Project project = new Project(projectTo.getName(), projectTo.getDescription(), projectTo.getDepartment(), projectTo.getManager(), projectTo.getAdministrator());
        project = projectService.save(project);
        URI uri = ServletUriComponentsBuilder.fromCurrentContextPath().path(REST_URL + "/{id}").build(project.getId());

        return ResponseEntity.created(uri).body(new ProjectTo(project));
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_USER')")
    @PutMapping("/{id}")
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void updateProject(@RequestBody ProjectTo projectTo, @PathVariable int id) {
        if (projectTo.getId() == null || projectTo.getId() != id)
            throw new IllegalRequestException("Project should be with id:" + id);

        Project project = projectService.get(id);
        project.setName(projectTo.getName());
        project.setAdministrator(projectTo.getAdministrator());
        project.setManager(projectTo.getManager());
        project.setDepartment(projectTo.getDepartment());
        project.setDescription(projectTo.getDescription());
        project.setParticipants(projectTo.getParticipants());
        projectService.save(project);
    }
}
