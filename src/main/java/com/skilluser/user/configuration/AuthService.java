package com.skilluser.user.configuration;

import com.skilluser.user.controller.LoginController;
import com.skilluser.user.dto.LoginRequest;
import com.skilluser.user.dto.LoginResponse;
import com.skilluser.user.dto.SignupResponseDto;
import com.skilluser.user.enums.AuthProviderType;
import com.skilluser.user.model.Role;
import com.skilluser.user.model.User;
import com.skilluser.user.repository.RoleRepository;
import com.skilluser.user.repository.UserRepository;
import com.skilluser.user.utility.AuthUtil;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.List;

@Component
public class AuthService {
    @Autowired
    private AuthUtil authUtil;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private JwtUtil jwtUtil;
    @Autowired
    private RoleRepository roleRepository;


    public SignupResponseDto signup(LoginRequest signupRequestDto){
        User user = userRepository.findByEmail(signupRequestDto.getEmail());

        if(user != null) throw new IllegalArgumentException("User already exists");

        User newUser = new User();
        newUser.setEmail(signupRequestDto.getEmail());
        newUser.setPassword(signupRequestDto.getPassword());
      //  newUser.setProviderType(AuthProviderType.LOCAL);    // new update
        user = userRepository.save(newUser);

        return new SignupResponseDto(user.getId(),user.getUsername());
    }

    @Transactional
    public ResponseEntity<LoginResponse> handlerOAuth2LoginRequest(OAuth2User oAuth2User, String registrationId,HttpServletResponse httpServletResponse)
    {
        // fetch providerType and providerId
        AuthProviderType providerType = authUtil.getProviderTypeFromRegistrationId(registrationId);
        String providerId = authUtil.determineProviderIdFromOAuth2User(oAuth2User,registrationId);
        String  name= oAuth2User.getAttribute("name");
        String email = authUtil.determineUsernameFromOAuth2User(oAuth2User, registrationId, providerId);
        String photo = oAuth2User.getAttribute("picture");

        // find user by providerId and providerType
        User user = userRepository.findByProviderIdAndProviderType(providerId,providerType).orElse(null);


        // find user by email
        User emailUser = userRepository.findByEmail(email);

        if(user == null && emailUser == null){
            // ----- NEW OAUTH USER → SIGNUP -----

            Role role = roleRepository.findById(3L).orElse(null);

            User newUser = new User();
            newUser.setUsername(name);
            newUser.setEmail(email);
            newUser.setPhoto(photo);
            newUser.setProviderId(providerId);
            newUser.setProviderType(providerType);
            newUser.setRoles(role);
            newUser.setPassword(null); // no password for OAuth

            user = userRepository.save(newUser);
        } else if(user != null){
            // ----- EXISTING OAUTH USER -----
            // update email/picture if changed
            if (email != null && !email.equals(user.getEmail())) {
                user.setEmail(email);
            }
            if (photo != null) user.setPhoto(photo);

            userRepository.save(user);
        } else {
            throw new BadCredentialsException("This email is already registered with provider "+emailUser.getProviderType());
        }
        // Now login → return LoginResponseDto
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setEmail(user.getEmail());
        loginRequest.setPassword(user.getPassword()); // null allowed for OAuth

        // Assign USER role
        List<String> roles = List.of("USER");

        // Generate JWT token
        String jwtToken = jwtUtil.generateToken(user.getId(), user.getEmail(), roles, user.getName(), user.getContact_no(), user.getCollegeId(), user.getCompanyId(), user.getDepartment());

         LoginResponse loginResponse = new LoginResponse();
        loginResponse.setUserId(user.getId());
        loginResponse.setCollegeId(user.getCollegeId());
        loginResponse.setCompanyId(user.getCompanyId());
        loginResponse.setEmail(user.getEmail());
        loginResponse.setUsername(user.getName());
        loginResponse.setDepartment(user.getDepartment());
        loginResponse.setHodverified(user.getHodverified());
        loginResponse.setContact_no(user.getContact_no());
        loginResponse.setPhoto(user.getPhoto());
        loginResponse.setToken(jwtToken);

        return ResponseEntity.ok(loginResponse);




        // save the providerType and provider id info with user

        // if the user has an account: directly login

        // otherwise, first sign up and then login
    }




}
