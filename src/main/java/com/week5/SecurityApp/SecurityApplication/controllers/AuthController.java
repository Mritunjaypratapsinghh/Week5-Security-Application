package com.week5.SecurityApp.SecurityApplication.controllers;


import com.week5.SecurityApp.SecurityApplication.advice.ApiResponse;
import com.week5.SecurityApp.SecurityApplication.dto.LoginDTO;
import com.week5.SecurityApp.SecurityApplication.dto.LoginResponseDTO;
import com.week5.SecurityApp.SecurityApplication.dto.SignupDTO;
import com.week5.SecurityApp.SecurityApplication.dto.UserDTO;
import com.week5.SecurityApp.SecurityApplication.entities.UserEntity;
import com.week5.SecurityApp.SecurityApplication.entities.enums.SubscriptionPlan;
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
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.Map;

/**
 * The type Auth controller.
 */
@RequiredArgsConstructor
@RestController
@RequestMapping("/auth")
public class AuthController {


    private final UserService userService;
    private final AuthService authService;

    @Value("${deploy.env}")
    private String deployEnv;

    /**
     * Sign up response entity.
     *
     * @param request the request
     * @return the response entity
     */
    @PostMapping("/signup")
    public ResponseEntity<UserDTO> signUp(@RequestBody SignupDTO request) {
        UserDTO userDTO = userService.signUp(request);
        return new ResponseEntity<>(userDTO, HttpStatus.CREATED);
    }

    /**
     * Login response entity.
     *
     * @param loginDTO the login dto
     * @param request  the request
     * @param response the response
     * @return the response entity
     */
    @PostMapping("/login")
    public ResponseEntity<ApiResponse<LoginResponseDTO>> login(@RequestBody LoginDTO loginDTO, HttpServletRequest request, HttpServletResponse response) {
        LoginResponseDTO loginResponseDTO = authService.login(loginDTO);
        Cookie cookie = new Cookie("refreshToken", loginResponseDTO.getRefreshToken());
        cookie.setHttpOnly(true);
        cookie.setSecure("production".equals(deployEnv));
        response.addCookie(cookie);
        return ResponseEntity.ok(new ApiResponse<>(loginResponseDTO));
    }

    /**
     * Refresh response entity.
     *
     * @param request the request
     * @return the response entity
     */
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

    /**
     * Logout response entity.
     *
     * @param refreshToken the refresh token
     * @param request      the request
     * @param response     the response
     * @return the response entity
     */
    @PostMapping("/logout")
    public ResponseEntity<ApiResponse<String>> logout(@CookieValue(value = "refreshToken", required = false) String refreshToken,
                                                      HttpServletRequest request, HttpServletResponse response) {
        boolean logout = authService.logout(refreshToken, request, response);
        if (logout) {
            return ResponseEntity.ok(new ApiResponse<>("Logout Successfully"));
        }
        return (ResponseEntity<ApiResponse<String>>) ResponseEntity.badRequest();
    }


    /**
     * Update subscription response entity.
     *
     * @param requestBody the request body
     * @param request     the request
     * @param response    the response
     * @return the response entity
     */
    @PostMapping("/subscription")
    public ResponseEntity<ApiResponse<UserDTO>> updateSubscription(@RequestBody Map<String, String> requestBody, HttpServletRequest request, HttpServletResponse response) {
        String subscriptionPlanString = requestBody.get("subscriptionPlan");
        System.out.println(requestBody);
        SubscriptionPlan subscriptionPlan = SubscriptionPlan.valueOf(subscriptionPlanString.toUpperCase());
        UserDTO userDTO = userService.updateUserSubscription(subscriptionPlan);
        return ResponseEntity.ok(new ApiResponse<>(userDTO));
    }


}

