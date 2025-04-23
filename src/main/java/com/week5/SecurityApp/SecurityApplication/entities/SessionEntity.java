package com.week5.SecurityApp.SecurityApplication.entities;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

/**
 * The type Session entity.
 */
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

    /**
     * Instantiates a new Session entity.
     *
     * @param user  the user
     * @param token the token
     */
    public SessionEntity(UserEntity user, String token) {
    }

    /**
     * Sets user id.
     *
     * @param userId the user id
     */
    public void setUserId(Long userId) {
    }
}