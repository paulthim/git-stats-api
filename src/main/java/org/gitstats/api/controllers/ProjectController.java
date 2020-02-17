package org.gitstats.api.controllers;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.gitstats.api.git.GitUtility;
import org.gitstats.api.models.Project;
import org.gitstats.api.models.User;
import org.gitstats.api.models.exceptions.ResourceNotFoundException;
import org.gitstats.api.repository.ProjectRepository;
import org.gitstats.api.repository.UserRepository;
import org.gitstats.api.security.CurrentUser;
import org.gitstats.api.security.UserPrincipal;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.io.IOException;
import java.util.Optional;

@RestController
@RequestMapping("/project")
public class ProjectController {

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private UserRepository userRepository;

    private static final Logger log = LoggerFactory.getLogger(ProjectController.class);

    @PostMapping("")
    @ResponseStatus(HttpStatus.CREATED)
    Project newProject(@Valid @RequestBody Project newProject, @CurrentUser UserPrincipal currentUser) {
        newProject.addUser(new User(currentUser.getId()));
        try {
            Git git = GitUtility.clone(newProject);
            newProject.setLatestCommitDate(GitUtility.getLatestCommitDate(git));
        } catch (GitAPIException | IOException e) {
            e.printStackTrace();
        }
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
    void deleteProject(@PathVariable Long id, Boolean deleteUsers) {
        Project project = projectRepository.findById(id).get();
        if (deleteUsers) {
            userRepository.deleteAll(project.getUsers());
        }
        boolean deleteResult = GitUtility.deleteRepoPath(project);
        if (!deleteResult) {
            log.error(String.format("Failed to delete local repository for project %s", project.getName()));
        }
        projectRepository.deleteById(id);
    }
}
