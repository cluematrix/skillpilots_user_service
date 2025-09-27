package com.skilluser.user.service;

import com.skilluser.user.model.User;

import java.util.List;

public interface UserService {
 public User getUserById(Long id);

    List<User> findUsersByRoleAndDepartment(Long roleId, Long departmentId);

    List<User> findUsersByRoles_IdAndCollegeId(Long roleId, Long collegeId);

    List<User> findHodByDepartment(Long roleId, Long departmentId);


}
