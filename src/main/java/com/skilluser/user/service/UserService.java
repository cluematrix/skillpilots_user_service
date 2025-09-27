package com.skilluser.user.service;

import com.skilluser.user.model.User;

import java.util.List;

public interface UserService {
 public User getUserById(Long id);
<<<<<<< HEAD

    List<User> findUsersByRoleAndDepartment(Long roleId, Long departmentId);

    List<User> findUsersByRoles_IdAndCollegeId(Long roleId, Long collegeId);

    List<User> findHodByDepartment(Long roleId, Long departmentId);

=======
 public User findByUsername(String username);
 public boolean changePassword(String email, String oldPassword, String newPassword);
 public User forgotPassword(String email);
>>>>>>> b45dd3c14b5f4290140d72ecae28211640eb0035

}
