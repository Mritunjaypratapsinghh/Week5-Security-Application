package com.week5.SecurityApp.SecurityApplication.services;

import com.week5.SecurityApp.SecurityApplication.dto.LoginDTO;
import com.week5.SecurityApp.SecurityApplication.dto.LoginResponseDTO;
import com.week5.SecurityApp.SecurityApplication.entities.SessionEntity;
import com.week5.SecurityApp.SecurityApplication.entities.UserEntity;
import com.week5.SecurityApp.SecurityApplication.repositories.SessionRepository;
import com.week5.SecurityApp.SecurityApplication.repositories.UserRepository;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

/**
 * The type Auth service.
 */
@Service
@RequiredArgsConstructor
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final SessionRepository sessionRepository;
    private final UserService userService;
    private final SessionService sessionService;
    private final UserRepository userRepository;

    /**
     * Login login response dto.
     *
     * @param loginDTO the login dto
     * @return the login response dto
     */
    public LoginResponseDTO login(LoginDTO loginDTO) {
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginDTO.getEmail(), loginDTO.getPassword()));
        UserEntity user = (UserEntity) authentication.getPrincipal();
        String accessToken = jwtService.generateAccessToken(user);
        String refreshToken = jwtService.generateRefreshToken(user);
        sessionService.generateNewSession(user, refreshToken);
        return new LoginResponseDTO(user.getId(), accessToken, refreshToken);
    }
    /**
     * Refresh token login response dto.
     *
     * @param oldRefreshToken the old refresh token
     * @return the login response dto
     */
    public LoginResponseDTO refreshToken(String oldRefreshToken) {

        Long userId = jwtService.getUserIdFromToken(oldRefreshToken);
        sessionService.validateSession(oldRefreshToken);
        sessionService.deleteSessionUsingRefreshToken(oldRefreshToken);
        UserEntity user = userService.getUserById(userId);

        String acccessToken = jwtService.generateAccessToken(user);
        String refreshToken = jwtService.generateRefreshToken(user);
        sessionService.generateNewSession(user,refreshToken);
        return new LoginResponseDTO(user.getId(), acccessToken, refreshToken);

    }

    /**
     * Logout boolean.
     *
     * @param refreshToken the refresh token
     * @param request      the request
     * @param response     the response
     * @return the boolean
     */
    public boolean logout(String refreshToken,HttpServletRequest request, HttpServletResponse response) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if(auth!=null){
            new SecurityContextLogoutHandler().logout(request,response,auth);
        }

        request.getSession().invalidate();

        String username = auth.getName();
        UserEntity userEntity = userRepository.findByEmail(username).orElse(null);
        sessionService.deleteSessionUsingRefreshToken(refreshToken);
        clearRefreshTokenCookie(response);
        return true;

    }

    /***
     *
     * @param response
     */
    private void clearRefreshTokenCookie(HttpServletResponse response){
        Cookie cookie = new Cookie("refreshToken", "");
        cookie.setMaxAge(0);
        response.addCookie(cookie);
    }
}
