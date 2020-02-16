package org.gitstats.api.controllers;

import org.gitstats.api.models.Project;
import org.gitstats.api.models.User;
import org.gitstats.api.repository.UserRepository;
import org.gitstats.api.security.CurrentUser;
import org.gitstats.api.security.UserPrincipal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @PostMapping("")
    @ResponseStatus(HttpStatus.CREATED)
    User newUser(@Valid @RequestBody User user, @CurrentUser UserPrincipal currentUser) {
        return userRepository.save(user);
    }

    @GetMapping("/projects")
    Iterable<Project> byUser(@CurrentUser UserPrincipal currentUser) {
        Set<Project> userProjects = new HashSet<>();
        Optional<User> existingUser = userRepository.findByEmail(currentUser.getEmail());
        if (existingUser.isPresent()) {
            userProjects = existingUser.get().getProjects();
        }
        return userProjects;
    }
}
