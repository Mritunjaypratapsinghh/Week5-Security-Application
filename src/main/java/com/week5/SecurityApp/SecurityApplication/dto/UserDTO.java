package com.week5.SecurityApp.SecurityApplication.dto;

import lombok.Data;

@Data
public class UserDTO {

    private Long id;
    private String email;
    private String password;
    private String name;
}
