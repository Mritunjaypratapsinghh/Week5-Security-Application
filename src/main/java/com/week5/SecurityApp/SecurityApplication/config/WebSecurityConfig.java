package com.week5.SecurityApp.SecurityApplication.config;


import com.week5.SecurityApp.SecurityApplication.entities.enums.Role;
import com.week5.SecurityApp.SecurityApplication.filters.JwtAuthFilter;
import com.week5.SecurityApp.SecurityApplication.handlers.OAuth2SuccessHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import static com.week5.SecurityApp.SecurityApplication.entities.enums.Permission.*;
import static com.week5.SecurityApp.SecurityApplication.entities.enums.Role.ADMIN;
import static com.week5.SecurityApp.SecurityApplication.entities.enums.Role.CREATOR;

@Configuration
@EnableWebSecurity
@EnableTransactionManagement
@RequiredArgsConstructor
@EnableMethodSecurity(securedEnabled = true)
public class WebSecurityConfig {

    private final JwtAuthFilter jwtAuthFilter;
    private final OAuth2SuccessHandler oAuth2SuccessHandler;
    private static final String[] publicRoutes = {
            "/posts", "/error", "/auth/**", "/home.html"
    };

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
                .authorizeHttpRequests(auth -> auth

                        .requestMatchers(publicRoutes).permitAll()

                      .requestMatchers(HttpMethod.GET, "/post/**").permitAll()
                      .requestMatchers(HttpMethod.POST,"/post").hasAnyRole(ADMIN.name(), CREATOR.name())
                      .requestMatchers(HttpMethod.POST,"/post").hasAuthority(POST_CREATE.name())
                      .requestMatchers(HttpMethod.DELETE,"/post/**").hasAuthority(POST_DELETE .name())
                        .anyRequest().authenticated())
                .csrf(csrfConfig -> csrfConfig.disable())
                .sessionManagement(
                        sessionConfig ->
                                sessionConfig.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)
                .oauth2Login(oauth2Config -> oauth2Config
                        .failureUrl("/login?error=true")
                        .successHandler(oAuth2SuccessHandler));



        return httpSecurity.build();
    }


    @Bean
    AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

}


