package com.skilluser.user.configuration;

import com.skilluser.user.serviceImpl.UserServiceImpl;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Component
@Slf4j
public class JwtFilter extends OncePerRequestFilter {

    @Autowired
    private JwtUtil jwtUtils;
//    @Autowired
//    private UserServiceImpl userService;
    @Autowired
    private JwtUtil jwtUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        String header = request.getHeader("Authorization");

        if (header == null || !header.startsWith("Bearer "))
        {
            filterChain.doFilter(request, response);
            return;
        }

        String token = header.substring(7);

        try {
            //  Extract username first
            Long id = jwtUtil.extractUserId(token);

            //  Validate token with username
            if (id != null && jwtUtil.validateToken(token, id)) {

                // Extract roles from token
                List<String> roles = jwtUtil.extractRoles(token);

                List<SimpleGrantedAuthority> authorities = roles.stream()
                        // If token contains ["COLLEGE"], use .hasAuthority("COLLEGE")
                        // If token contains ["ROLE_COLLEGE"], use .hasRole("COLLEGE")
                        .map(SimpleGrantedAuthority::new)
                        .collect(Collectors.toList());

                UsernamePasswordAuthenticationToken authentication =
                        new UsernamePasswordAuthenticationToken(id, null, authorities);

                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                //  Set authentication in context
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }

        } catch (Exception ex) {

            SecurityContextHolder.clearContext();
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setContentType("application/json");
            response.getWriter().write(
                    "{\"error\":\"Unauthorized\",\"message\":\"Invalid or expired token\"}"
            );
            return;
        }

        filterChain.doFilter(request, response);
    }

}


