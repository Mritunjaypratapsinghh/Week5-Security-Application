package com.week5.SecurityApp.SecurityApplication.filters;

import com.week5.SecurityApp.SecurityApplication.entities.SessionEntity;
import com.week5.SecurityApp.SecurityApplication.entities.UserEntity;
import com.week5.SecurityApp.SecurityApplication.repositories.SessionRepository;
import com.week5.SecurityApp.SecurityApplication.services.JwtService;

import com.week5.SecurityApp.SecurityApplication.services.UserService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.servlet.HandlerExceptionResolver;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.logging.Handler;

/**
 * The type Jwt auth filter.
 */
@Component
public class JwtAuthFilter extends OncePerRequestFilter {
    private final JwtService jwtService;
    private final UserService userService;
    private final SessionRepository sessionRepository;

    /**
     * Instantiates a new Jwt auth filter.
     *
     * @param jwtService        the jwt service
     * @param userService       the user service
     * @param sessionRepository the session repository
     */
    public JwtAuthFilter(JwtService jwtService,UserService userService,SessionRepository sessionRepository){
        this.jwtService = jwtService;
        this.userService = userService;
        this.sessionRepository = sessionRepository;
    }


    @Autowired
    @Qualifier("handlerExceptionResolver")
    private HandlerExceptionResolver handlerExceptionResolver;

    /***
     *
     * @param request
     * @param response
     * @param filterChain
     * @throws ServletException
     * @throws IOException
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try{

        final String requestTokenHeader = request.getHeader("Authorization");
        if(requestTokenHeader == null || !requestTokenHeader.startsWith(("Bearer"))){
            filterChain.doFilter(request,response);
            return;
        }

        String token = requestTokenHeader.split("Bearer ")[1];

        Long userId = jwtService.getUserIdFromToken(token);


        if(userId!=null && SecurityContextHolder.getContext().getAuthentication()==null){
            UserEntity user =userService.getUserById(userId);

            UsernamePasswordAuthenticationToken authenticationToken =
                    new UsernamePasswordAuthenticationToken(user,null,user.getAuthorities());


            authenticationToken.setDetails( new WebAuthenticationDetailsSource().buildDetails(request));

            SecurityContextHolder.getContext().setAuthentication(authenticationToken);
        }

        filterChain.doFilter(request,response);

        }
        catch (Exception exception){
        handlerExceptionResolver.resolveException(request,response,null,exception);


    }}
    
}
