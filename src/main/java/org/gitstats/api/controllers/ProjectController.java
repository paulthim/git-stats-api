package org.gitstats.api.controllers;

import org.gitstats.api.models.Project;
import org.gitstats.api.models.User;
import org.gitstats.api.models.exceptions.ResourceNotFoundException;
import org.gitstats.api.repository.ProjectRepository;
import org.gitstats.api.repository.UserRepository;
import org.gitstats.api.security.CurrentUser;
import org.gitstats.api.security.UserPrincipal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Optional;

@RestController
@RequestMapping("/project")
public class ProjectController {

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private UserRepository userRepository;

    @PostMapping("")
    @ResponseStatus(HttpStatus.CREATED)
    Project newProject(@Valid @RequestBody Project newProject, @CurrentUser UserPrincipal currentUser) {
        newProject.addUser(new User(currentUser.getId()));
        return projectRepository.save(newProject);
    }

    @GetMapping("/{projectId}")
    Optional<Project> byId(@PathVariable Long projectId) {
        return projectRepository.findById(projectId);
    }

    @PutMapping("/{projectId}")
    Project update(@RequestBody Project updatedProject, @PathVariable Long projectId, @CurrentUser UserPrincipal currentUser) {
        return projectRepository.findById(projectId)
                .map(project -> {
                    if (project.hasUserWithId(currentUser.getId())) {
                        project.update(updatedProject);
                    }
                    return projectRepository.save(project);
                })
                .orElseThrow(() -> new ResourceNotFoundException("Project", "id", projectId));
    }

    @DeleteMapping("/{id}")
    void deleteEmployeeProject(@PathVariable Long id, Boolean deleteUsers) {
        if (deleteUsers) {
            Project project = projectRepository.findById(id).get();
            userRepository.deleteAll(project.getUsers());
        }
        projectRepository.deleteById(id);
    }
}
