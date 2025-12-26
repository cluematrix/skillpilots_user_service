package com.skilluser.user.service;

import com.skilluser.user.model.User;

import java.util.List;
import java.util.Map;

public interface UserService {
 public User getUserById(Long id);

    List<User> findUsersByRoleAndDepartment(Long roleId, Long departmentId);

    List<User> findUsersByRoles_IdAndCollegeId(Long roleId, Long collegeId);

    List<User> findHodByDepartment(Long roleId, Long departmentId);


 public User findByUsername(String username);
 public boolean changePassword(Long userId, String oldPassword, String newPassword);
 public User forgotPassword(String email);

 public User getAllDataByUserId(Long userId);

 public Map<String, Object> checkPayment(Long userId);


}
