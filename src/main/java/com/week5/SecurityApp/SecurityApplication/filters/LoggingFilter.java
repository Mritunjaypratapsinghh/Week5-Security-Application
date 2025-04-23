package com.week5.SecurityApp.SecurityApplication.filters;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Enumeration;

/**
 * The type Logging filter.
 */
@Component
public class LoggingFilter extends OncePerRequestFilter {

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

        logger.info("Incoming Request: {} {}");

        Enumeration<String> headerNames = request.getHeaderNames();
        logger.info("Headers");
        while (headerNames.hasMoreElements()){
            String headerName = headerNames.nextElement();
            logger.info("- {}: {}");
        }

        filterChain.doFilter(request,response);

        logger.info(" Outgoing Response: {} {}");
        logger.info("Status Code: {}");
    }
}
