package org.gitstats.api.models.dto;

import org.gitstats.api.models.UserRole;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

public class UserRequest {
    private String name;

    @NotBlank
    @Email(message = "Please enter valid email")
    private String email;

    @NotBlank
    @Size(min = 1, max = 255, message = "Please enter valid password")
    private String password;

    private UserRole userRole = UserRole.STUDENT;

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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public UserRole getUserRole() {
        return userRole;
    }

    public void setUserRole(UserRole userRole) {
        this.userRole = userRole;
    }
}
