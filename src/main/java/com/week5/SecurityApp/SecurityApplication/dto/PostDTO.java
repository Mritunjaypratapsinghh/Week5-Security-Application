package com.week5.SecurityApp.SecurityApplication.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class PostDTO {
    private Long id;
    private String name;
    private LocalDateTime createdAt;
    private UserDTO author;
}
