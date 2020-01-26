package org.gitstats.api.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.gitstats.api.models.dto.UserRequest;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
public class User extends Audit {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String name;

    @NotNull
    @Column(unique = true)
    private String email;

    @NotNull
    private UserRole userRole;

    @JsonIgnore
    private String password;

    public User() {
    }

    public User(Long id) {
        this.id = id;
    }

    public User(UserRequest userRequest) {
        this.name = userRequest.getName();
        this.email = userRequest.getEmail();
        this.userRole = userRequest.getUserRole();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public UserRole getUserRole() {
        return userRole;
    }

    public void setUserRole(UserRole userRole) {
        this.userRole = userRole;
    }

    public User update(UserRequest updatedUser) {
        if (updatedUser.getName() != null) {
            this.setName(updatedUser.getName());
        }

        if (updatedUser.getUserRole() != null) {
            this.setUserRole(updatedUser.getUserRole());
        }
        return this;
    }
}
