package com.skilluser.user.configuration;

import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.DelegatingPasswordEncoder;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.OAuth2AuthorizationSuccessHandler;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.skilluser.user.serviceImpl.UserServiceImpl;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;
/*

@Configuration
public class AppConfig {

    @Autowired
    private JwtFilter jwtFilter;
    @Autowired
    private UserServiceImpl userService;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/v1/auth/**").permitAll()
                        .requestMatchers("/api/v1/students/register",
                                "/api/v1/students/setPassword", "/api/v1/students/resendOtp").permitAll()
                        .requestMatchers("/api/v1/users/**").permitAll()
                        .requestMatchers("/api/v1/colleges/**").hasAuthority("COLLEGE")   // 👈 changed
                        .requestMatchers("/api/v1/students/**").hasAuthority("INT_STUDENT") // 👈 changed
                        .requestMatchers("/api/v1/companies/**").hasAuthority("COMPANY")  // 👈 changed
                        .anyRequest().authenticated()
                )
                .exceptionHandling(ex -> ex
                        .authenticationEntryPoint((request, response, authException) -> {
                            response.setContentType("application/json");
                            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                            response.getWriter().write("{\"error\":\"Unauthorized\", \"message\":\""
                                    + authException.getMessage() + "\"}");
                        })
                        .accessDeniedHandler((request, response, accessDeniedException) -> {
                            response.setContentType("application/json");
                            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
                            response.getWriter().write("{\"error\":\"Forbidden\", \"message\":\"Access denied\"}");
                        })
                )
                .authenticationProvider(authenticationProvider())
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)
                .csrf(csrf -> csrf.disable());

        return http.build();
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
        daoAuthenticationProvider.setUserDetailsService(userService);
        ;
        daoAuthenticationProvider.setPasswordEncoder(passwordEncoder());
        return daoAuthenticationProvider;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
//@Bean
//public PasswordEncoder passwordEncoder() {
//    Map<String, PasswordEncoder> encoders = new HashMap<>();
//    encoders.put("bcrypt", new BCryptPasswordEncoder());
//    encoders.put("plain", NoOpPasswordEncoder.getInstance());
//
//    return new DelegatingPasswordEncoder("bcrypt", encoders);
//}

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public ModelMapper modelMapper(){
        return new ModelMapper();
    }

}*/

@Configuration
@EnableWebSecurity
@Slf4j
public class AppConfig {

//    @Autowired
//    private OAuth2SuccessHandler oAuth2SuccessHandler;
    @Autowired
    private JwtFilter jwtFilter;

    // old method for backup
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http, JwtFilter jwtFilter,
                                                   AuthenticationProvider authenticationProvider) throws Exception {
        http
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/v1/auth/**").permitAll()
                        .requestMatchers("/api/v1/colleges/**").hasRole("COLLEGE")
                        .anyRequest().permitAll()
                )
                .exceptionHandling(ex -> ex
                        .authenticationEntryPoint((request, response, authException) -> {
                            response.setContentType("application/json");
                            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                            response.getWriter().write("{\"error\":\"Unauthorized\", \"message\":\""
                                    + authException.getMessage() + "\"}");
                        })
                        .accessDeniedHandler((request, response, accessDeniedException) -> {
                            response.setContentType("application/json");
                            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
                            response.getWriter().write("{\"error\":\"Forbidden\", \"message\":\"Access denied\"}");
                        })
                )
                .authenticationProvider(authenticationProvider)
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)
                .csrf(csrf -> csrf.disable());

        return http.build();
    }




    @Bean
    public AuthenticationProvider authenticationProvider(UserServiceImpl userService,
                                                         PasswordEncoder passwordEncoder) {
        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
        daoAuthenticationProvider.setUserDetailsService(userService);
        daoAuthenticationProvider.setPasswordEncoder(passwordEncoder);
        return daoAuthenticationProvider;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration)
            throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public ModelMapper modelMapper() {
        return new ModelMapper();
    }
    @Bean
    public RestTemplate restTemplate(){
        return new RestTemplate();
    }
}

