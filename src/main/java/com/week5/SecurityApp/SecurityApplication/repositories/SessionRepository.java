package com.week5.SecurityApp.SecurityApplication.repositories;

import com.week5.SecurityApp.SecurityApplication.entities.SessionEntity;
import com.week5.SecurityApp.SecurityApplication.entities.UserEntity;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SessionRepository extends JpaRepository<SessionEntity,Long> {
    Optional<SessionEntity> findByUser(UserEntity user);

    @Transactional
    void deleteByUser(UserEntity user);

}
