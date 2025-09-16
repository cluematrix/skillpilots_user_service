package com.skilluser.user.service;

import com.skilluser.user.model.User;

public interface UserService {
 public User getUserById(Long id);
 public User findByUsername(String username);
}
