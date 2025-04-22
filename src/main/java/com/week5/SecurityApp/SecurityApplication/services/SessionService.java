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

@Service
@RequiredArgsConstructor
public class SessionService {

    private final SessionRepository sessionRepository;
    private final int SESSION_LIMIT = 2;

    public void generateNewSession(UserEntity userEntity, String refreshToken) {
        List<SessionEntity> userSessions = sessionRepository.findByUser(userEntity);
        if (userSessions.size() == SESSION_LIMIT) {
            userSessions.sort(Comparator.comparing(SessionEntity::getLastUsedAt));

            SessionEntity leastRecentlyUsedSession = userSessions.getFirst();
            sessionRepository.delete(leastRecentlyUsedSession);
        }

        SessionEntity newSession = SessionEntity.builder().user(userEntity).refreshToken(refreshToken)
                .build();

        sessionRepository.save(newSession);

    }

    public void validateSession(String refreshToken) {
        SessionEntity sessionEntity = sessionRepository.findByRefreshToken(refreshToken).orElseThrow(() ->
                new SessionAuthenticationException("Session not found for refreshToken"+refreshToken));
        sessionEntity.setLastUsedAt(LocalDateTime.now());
        sessionRepository.save(sessionEntity);
    }

}
