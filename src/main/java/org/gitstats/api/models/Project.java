package org.gitstats.api.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "project")
public class Project extends Audit {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    private String remoteUrl;
    private String name;

    @ManyToMany(mappedBy = "projects")
    @JsonIgnoreProperties("projects")
    private Set<User> users = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getRemoteUrl() {
        return remoteUrl;
    }

    public void setRemoteUrl(String remoteUrl) {
        this.remoteUrl = remoteUrl;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<User> getUsers() {
        return users;
    }

    public void setUsers(Set<User> users) { this.users = users; }

    public void addUser(User user) { this.users.add(user); }

    public Boolean hasUserWithId(long userId) {
        for (User user : users) {
            if (user.getId().equals(userId))
                return true;
        }
        return false;
    }

    public User getUserById(long userId) {
        for (User user : users) {
            if (user.getId().equals(userId))
                return user;
        }
        return null;
    }

    public void update(Project updatedProject) {
        this.setRemoteUrl(updatedProject.getRemoteUrl());
//        this.setGitUserName(updatedProject.getGitUserName());
//        this.setGitPassword(updatedProject.getGitPassword());
        this.setName(updatedProject.getName());
    }
}
