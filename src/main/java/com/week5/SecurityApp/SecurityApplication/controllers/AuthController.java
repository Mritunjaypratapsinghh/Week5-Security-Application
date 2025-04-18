package com.week5.SecurityApp.SecurityApplication.controllers;


import com.week5.SecurityApp.SecurityApplication.advice.ApiResponse;
import com.week5.SecurityApp.SecurityApplication.dto.LoginDTO;
import com.week5.SecurityApp.SecurityApplication.dto.SignupDTO;
import com.week5.SecurityApp.SecurityApplication.dto.UserDTO;
import com.week5.SecurityApp.SecurityApplication.services.AuthService;
import com.week5.SecurityApp.SecurityApplication.services.UserService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/auth")
public class AuthController {


    private final UserService userService;
    private final AuthService authService;


    @PostMapping("/signup")
    public ResponseEntity<UserDTO> signUp(@RequestBody SignupDTO request){
        UserDTO userDTO = userService.signUp(request);
        return new ResponseEntity<>(userDTO, HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<String>> login(@RequestBody LoginDTO loginDTO, HttpServletRequest request, HttpServletResponse response){
        String token = authService.login(loginDTO);
        Cookie cookie = new Cookie("token", token);
        cookie.setHttpOnly(true);
        response.addCookie(cookie);
        return ResponseEntity.ok(new ApiResponse<>(token));
    }
}
