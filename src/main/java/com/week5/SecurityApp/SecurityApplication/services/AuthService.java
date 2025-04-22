package com.week5.SecurityApp.SecurityApplication.services;

import com.week5.SecurityApp.SecurityApplication.dto.LoginDTO;
import com.week5.SecurityApp.SecurityApplication.dto.LoginResponseDTO;
import com.week5.SecurityApp.SecurityApplication.entities.SessionEntity;
import com.week5.SecurityApp.SecurityApplication.entities.UserEntity;
import com.week5.SecurityApp.SecurityApplication.repositories.SessionRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final SessionRepository sessionRepository;
    private final UserService userService;
    private final SessionService sessionService;

    public LoginResponseDTO login(LoginDTO loginDTO) {
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginDTO.getEmail(), loginDTO.getPassword()));
        UserEntity user = (UserEntity) authentication.getPrincipal();
        String accessToken = jwtService.generateAccessToken(user);
        String refreshToken = jwtService.generateRefreshToken(user);
        sessionService.generateNewSession(user,refreshToken);
        return new LoginResponseDTO(user.getId(), accessToken, refreshToken);


    }

//    @Transactional
//    public void storeSession(UserEntity user, String token){
//        Optional<SessionEntity> existingSession = sessionRepository.findByUser(user);
//
//        if(existingSession.isPresent()){
//            SessionEntity sessionEntity = existingSession.get();
//            sessionEntity.setToken(token);
//            sessionEntity.setLastUpdated(LocalDateTime.now());
//            sessionRepository.save(sessionEntity);
//        }
//        else{
//        SessionEntity sessionEntity = new SessionEntity();
//        sessionEntity.setToken(token);
//        sessionEntity.setUser(user);
//        sessionEntity.setCreatedAt(LocalDateTime.now());
//        sessionEntity.setLastUpdated(LocalDateTime.now());
//        sessionRepository.save(sessionEntity);
//    }
//    }

    public LoginResponseDTO refreshToken(String refreshToken) {

        Long userId = jwtService.getUserIdFromToken(refreshToken);
        sessionService.validateSession(refreshToken);
        UserEntity user = userService.getUserById(userId);

        String acccessToken = jwtService.generateAccessToken(user);
        return new LoginResponseDTO(user.getId(), acccessToken, refreshToken);

    }
}
