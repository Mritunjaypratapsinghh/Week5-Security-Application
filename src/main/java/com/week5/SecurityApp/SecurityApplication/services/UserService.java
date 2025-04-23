package com.week5.SecurityApp.SecurityApplication.services;

import com.week5.SecurityApp.SecurityApplication.dto.LoginDTO;
import com.week5.SecurityApp.SecurityApplication.dto.SignupDTO;
import com.week5.SecurityApp.SecurityApplication.dto.UserDTO;
import com.week5.SecurityApp.SecurityApplication.entities.UserEntity;
import com.week5.SecurityApp.SecurityApplication.entities.enums.SubscriptionPlan;
import com.week5.SecurityApp.SecurityApplication.exceptions.ResourceNotFoundException;
import com.week5.SecurityApp.SecurityApplication.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.boot.Banner;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * The type User service.
 */
@RequiredArgsConstructor
@Service
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;
    private final ModelMapper modelMapper;
    private final PasswordEncoder passwordEncoder;


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByEmail(username).orElseThrow(() -> new AuthenticationException("User with email: " + username + " not found") {
        });
    }

    /**
     * Gets user by email.
     *
     * @param email the email
     * @return the user by email
     */
    public UserEntity getUserByEmail(String email) {
        return userRepository.findByEmail(email).orElse(null);
    }

    /**
     * Sign up user dto.
     *
     * @param request the request
     * @return the user dto
     */
    public UserDTO signUp(SignupDTO request) {
        Optional<UserEntity> user = userRepository.findByEmail(request.getEmail());
        if (user.isPresent()) {
            throw new BadCredentialsException("User with email already exists " + request.getEmail());
        }
        UserEntity userEntity = modelMapper.map(request, UserEntity.class);
        userEntity.setPassword(passwordEncoder.encode(userEntity.getPassword()));
        UserEntity newUser = userRepository.save(userEntity);
        return modelMapper.map(newUser, UserDTO.class);
    }


    /**
     * Gets user by id.
     *
     * @param userId the user id
     * @return the user by id
     */
    public UserEntity getUserById(Long userId) {
        return userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User with userId: " + userId + "not found"));
    }


    /**
     * Save user entity.
     *
     * @param newUser the new user
     * @return the user entity
     */
    public UserEntity save(UserEntity newUser) {
        return userRepository.save(newUser);
    }


    /**
     * Update user subscription user dto.
     *
     * @param newPlan the new plan
     * @return the user dto
     */
    public UserDTO updateUserSubscription(SubscriptionPlan newPlan) {

        UserEntity userEntity = (UserEntity) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Long userId = userEntity.getId();
        userEntity.setSubscriptionPlan(newPlan);
        userRepository.save(userEntity);
        return modelMapper.map(userEntity, UserDTO.class);

    }

}

