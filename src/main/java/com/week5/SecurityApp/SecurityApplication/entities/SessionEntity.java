package com.week5.SecurityApp.SecurityApplication.entities;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
//@RequiredArgsConstructor
public class SessionEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private UserEntity user;

    @Column(nullable = false)
    private String refreshToken;

    @CreationTimestamp
    private LocalDateTime lastUsedAt;

    public SessionEntity(UserEntity user, String token) {
    }

    public void setUserId(Long userId) {
    }
}