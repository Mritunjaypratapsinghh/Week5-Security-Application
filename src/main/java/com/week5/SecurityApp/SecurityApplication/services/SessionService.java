package com.week5.SecurityApp.SecurityApplication.services;

import com.week5.SecurityApp.SecurityApplication.entities.SessionEntity;
import com.week5.SecurityApp.SecurityApplication.entities.UserEntity;
import com.week5.SecurityApp.SecurityApplication.repositories.SessionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.web.authentication.session.SessionAuthenticationException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

/**
 * The type Session service.
 */
@Service
@RequiredArgsConstructor
public class SessionService {

    private final SessionRepository sessionRepository;

    /***
     *
     * @param userEntity
     * @return
     */
    private int getSessionLimitBasedOnPlan(UserEntity userEntity) {
        // Get the session limit based on the user's subscription plan
        switch (userEntity.getSubscriptionPlan()) {
            case FREE:
                return 1;
            case BASIC:
                return 3;
            case PREMIUM:
                return 5;
            default:
                return 1;
        }
    }


    /**
     * Generate new session.
     *
     * @param userEntity   the user entity
     * @param refreshToken the refresh token
     */
    public void generateNewSession(UserEntity userEntity, String refreshToken) {

        List<SessionEntity> userSessions = sessionRepository.findByUser(userEntity);


        int sessionLimit = getSessionLimitBasedOnPlan(userEntity);


        if (userSessions.size() >= sessionLimit) {

            userSessions.sort(Comparator.comparing(SessionEntity::getLastUsedAt));

            SessionEntity leastRecentlyUsedSession = userSessions.get(0);


            sessionRepository.delete(leastRecentlyUsedSession);
        }


        SessionEntity newSession = SessionEntity.builder()
                .user(userEntity)
                .refreshToken(refreshToken)
                .build();

        sessionRepository.save(newSession);
    }

    /**
     * Validate session.
     *
     * @param refreshToken the refresh token
     */
    public void validateSession(String refreshToken) {
        SessionEntity sessionEntity = sessionRepository.findByRefreshToken(refreshToken).orElseThrow(() ->
                new SessionAuthenticationException("Session not found for refreshToken" + refreshToken));
        sessionEntity.setLastUsedAt(LocalDateTime.now());
        sessionRepository.save(sessionEntity);
    }


    /**
     * Delete session using refresh token.
     *
     * @param refreshToken the refresh token
     */
    public void deleteSessionUsingRefreshToken(String refreshToken) {
        SessionEntity sessionEntity = sessionRepository.findByRefreshToken(refreshToken).orElse(null);
        if (sessionEntity != null) {
            sessionRepository.delete(sessionEntity);
        }

    }

}
