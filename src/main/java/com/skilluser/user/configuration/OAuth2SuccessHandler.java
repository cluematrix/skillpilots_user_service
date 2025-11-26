//package com.skilluser.user.configuration;
//
//import com.fasterxml.jackson.databind.ObjectMapper;
//import com.skilluser.user.dto.LoginResponse;
//import jakarta.servlet.ServletException;
//import jakarta.servlet.http.HttpServletRequest;
//import jakarta.servlet.http.HttpServletResponse;
//import lombok.RequiredArgsConstructor;
//import org.modelmapper.ModelMapper;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.MediaType;
//import org.springframework.http.ResponseEntity;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
//import org.springframework.security.oauth2.core.user.OAuth2User;
//import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
//import org.springframework.stereotype.Component;
//
//import java.io.IOException;
//
//@Component
//@RequiredArgsConstructor
//public class OAuth2SuccessHandler implements AuthenticationSuccessHandler {
//    @Autowired
//    private AuthService authService;
//    @Autowired
//    private ObjectMapper objectMapper;
//
////    @Override
////    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
////
////        OAuth2AuthenticationToken token = (OAuth2AuthenticationToken) authentication;
////        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();
////
////        String registrationId = token.getAuthorizedClientRegistrationId();
////
////        ResponseEntity<LoginResponse> loginResponse = authService.handlerOAuth2LoginRequest(oAuth2User,registrationId,response);
////
////        response.setStatus(loginResponse.getStatusCode().value());
////        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
////        response.getWriter().write(objectMapper.writeValueAsString(loginResponse.getBody()));
////    }
//}
