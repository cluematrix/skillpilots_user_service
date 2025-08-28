package com.skilluser.user.configuration;

import com.skilluser.user.serviceImpl.UserServiceImpl;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@Slf4j
public class JwtFilter extends OncePerRequestFilter {

    @Autowired
    private JwtUtil jwtUtils;
    @Autowired
    private UserServiceImpl userService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        final String autHeader = request.getHeader("Authorization");
        final String jwtToken;
        final String userEmail;
        if(autHeader == null || autHeader.isBlank()) {
            filterChain.doFilter(request, response);
            return;
        }
        if (request.getRequestURI().contains("/user/login") || request.getRequestURI().contains("/user/signup")) {
            filterChain.doFilter(request, response);
            return;
        }

        jwtToken = autHeader.substring(7);
        userEmail = jwtUtils.extractUsername(jwtToken);
        if(userEmail !=null && SecurityContextHolder.getContext().getAuthentication()==null) {
            UserDetails userDetails  = userService.loadUserByUsername(userEmail);
            
            log.info("User details  "+userDetails);
            

            if(jwtUtils.isTokenValid(jwtToken, userDetails)) {
                SecurityContext securityContext  = SecurityContextHolder.createEmptyContext();
                UsernamePasswordAuthenticationToken token =  new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                token.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                securityContext.setAuthentication(token);
                SecurityContextHolder.setContext(securityContext);
            }
        }
        filterChain.doFilter(request, response);

    }
    }


