package com.week5.SecurityApp.SecurityApplication.controllers;


import com.week5.SecurityApp.SecurityApplication.advice.ApiResponse;
import com.week5.SecurityApp.SecurityApplication.dto.LoginDTO;
import com.week5.SecurityApp.SecurityApplication.dto.LoginResponseDTO;
import com.week5.SecurityApp.SecurityApplication.dto.SignupDTO;
import com.week5.SecurityApp.SecurityApplication.dto.UserDTO;
import com.week5.SecurityApp.SecurityApplication.services.AuthService;
import com.week5.SecurityApp.SecurityApplication.services.UserService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;

@RequiredArgsConstructor
@RestController
@RequestMapping("/auth")
public class AuthController {


    private final UserService userService;
    private final AuthService authService;

    @Value("${deploy.env}")
    private String deployEnv;

    @PostMapping("/signup")
    public ResponseEntity<UserDTO> signUp(@RequestBody SignupDTO request) {
        UserDTO userDTO = userService.signUp(request);
        return new ResponseEntity<>(userDTO, HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<LoginResponseDTO>> login(@RequestBody LoginDTO loginDTO, HttpServletRequest request, HttpServletResponse response) {
        LoginResponseDTO loginResponseDTO = authService.login(loginDTO);
        Cookie cookie = new Cookie("refreshToken", loginResponseDTO.getRefreshToken());
        cookie.setHttpOnly(true);
        cookie.setSecure("production".equals(deployEnv));
        response.addCookie(cookie);
        return ResponseEntity.ok(new ApiResponse<>(loginResponseDTO));
    }

    @PostMapping("/refresh")
    public ResponseEntity<ApiResponse<LoginResponseDTO>> refresh(HttpServletRequest request) {

        String refreshToken = Arrays.stream(request.getCookies()).
                filter(cookie -> "refreshToken".equals(cookie.getName()))
                .findFirst().
                map(Cookie::getValue)
                .orElseThrow(() -> new AuthenticationServiceException("Refresh token not found inside the cookies"));


        LoginResponseDTO loginResponseDTO = authService.refreshToken(refreshToken);
        return ResponseEntity.ok(new ApiResponse<>(loginResponseDTO));
    }
     
}
