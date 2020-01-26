package org.gitstats.api.models;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
public class Project extends Audit {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    private String gitUrl;
    private String gitUserName;
    private String gitPassword;
    private String name;

    @ManyToOne()
    @JoinColumn(name = "user_id")
    private User owner;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getGitUrl() {
        return gitUrl;
    }

    public void setGitUrl(String gitUrl) {
        this.gitUrl = gitUrl;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public User getOwner() {
        return owner;
    }

    public void setOwner(User owner) {
        this.owner = owner;
    }

    public String getGitUserName() {
        return gitUserName;
    }

    public void setGitUserName(String gitUserName) {
        this.gitUserName = gitUserName;
    }

    public String getGitPassword() {
        return gitPassword;
    }

    public void setGitPassword(String gitPassword) {
        this.gitPassword = gitPassword;
    }

    public void update(Project updatedProject) {
        this.setGitUrl(updatedProject.getGitUrl());
        this.setGitUserName(updatedProject.getGitUserName());
        this.setGitPassword(updatedProject.getGitPassword());
        this.setName(updatedProject.getName());
    }
}
