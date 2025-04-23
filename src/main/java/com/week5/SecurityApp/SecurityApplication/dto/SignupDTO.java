package com.week5.SecurityApp.SecurityApplication.dto;

import com.week5.SecurityApp.SecurityApplication.entities.enums.Permission;
import com.week5.SecurityApp.SecurityApplication.entities.enums.Role;
import lombok.Data;

import java.util.Set;

/**
 * The type Signup dto.
 */
@Data
public class SignupDTO {

    private String email;
    private String password;
    private String name;
    private Set<Role> roles;
    private Set<Permission> permissions;

}
