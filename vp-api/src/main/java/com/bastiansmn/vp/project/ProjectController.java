package com.bastiansmn.vp.project;

import com.bastiansmn.vp.exception.FunctionalException;
import com.bastiansmn.vp.exception.TechnicalException;
import com.bastiansmn.vp.project.dto.ProjectCreationDTO;
import com.bastiansmn.vp.user.UserDAO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.Collection;

@RestController
@Slf4j
@RequestMapping("/project")
@RequiredArgsConstructor
public class ProjectController {

    private final ProjectService projectService;

    @PostMapping("/create")
    public ResponseEntity<ProjectDAO> create(@RequestBody ProjectCreationDTO project) throws FunctionalException {
        URI uri = URI.create(
                ServletUriComponentsBuilder
                        .fromCurrentContextPath()
                        .path("/api/v1/project/create")
                        .toUriString()
        );
        return ResponseEntity.created(uri).body(this.projectService.create(project));
    }

    @GetMapping("/fetchById")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<ProjectDAO> fetchById(@RequestParam String project_id) throws FunctionalException {
        return ResponseEntity.ok(this.projectService.fetchById(project_id));
    }

    @GetMapping("/fetchAll")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<Collection<ProjectDAO>> fetchAll() {
        return ResponseEntity.ok(this.projectService.fetchAll());
    }

    @DeleteMapping("/deleteById")
    public ResponseEntity<Void> deleteById(@RequestParam String project_id) throws FunctionalException {
        this.projectService.deleteById(project_id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/myProjects")
    public ResponseEntity<Collection<ProjectDAO>> myProjects() throws FunctionalException {
        String userContext = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return ResponseEntity.ok(this.projectService.fetchProjectsOfUser(userContext));
    }

    @GetMapping("/fetchProjectsOf")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<Collection<ProjectDAO>> fetchProjectsOfUser(@RequestParam String user_email) throws FunctionalException {
        return ResponseEntity.ok(this.projectService.fetchProjectsOfUser(user_email));
    }

    @PostMapping("/addUserToProject")
    public ResponseEntity<UserDAO> addUserToProject(@RequestParam String user_email, @RequestParam String project_id)
            throws FunctionalException, TechnicalException {
        return ResponseEntity.ok(this.projectService.addUserToProject(project_id, user_email));
    }

}
