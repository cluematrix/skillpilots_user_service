package com.skilluser.user.controller;

import com.skilluser.user.dto.UserDto;
import com.skilluser.user.model.User;
import com.skilluser.user.service.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/users")
public class UserController {

    public final UserService userService;
    public final ModelMapper modelMapper;
    public UserController(UserService userService, ModelMapper modelMapper) {
        this.userService = userService;
        this.modelMapper = modelMapper;
    }


    @GetMapping("{id}")
    public ResponseEntity<UserDto> getByUserId(@PathVariable Long id) {
        try {
            User user = userService.getUserById(id);
            UserDto dto = new UserDto();
            dto.setId(user.getId());
            dto.setName(user.getUsername());
            dto.setEmail(user.getEmail());

            if (user.getRoles() != null) {
                dto.setRole(user.getRoles().getName());
            }

            dto.setCollegeId(user.getCollegeId());

            return ResponseEntity.ok(dto);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }


}
