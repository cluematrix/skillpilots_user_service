package com.skilluser.user.service;

import com.skilluser.user.model.BusinessUser;
import com.skilluser.user.model.ContactRequest;
import com.skilluser.user.model.User;

import java.util.List;
import java.util.Map;

public interface UserService {
    User getUserById(Long id);

    List<User> findUsersByRoleAndDepartment(Long roleId, Long departmentId);

    List<User> findUsersByRoles_IdAndCollegeId(Long roleId, Long collegeId);

    List<User> findHodByDepartment(Long roleId, Long departmentId);


    User findByUsername(String username);

    boolean changePassword(Long userId, String oldPassword, String newPassword);

    User forgotPassword(String email);

    User getAllDataByUserId(Long userId);

    Map<String, Object> checkPayment(Long userId);

    Map<String, Object> getPlanAmount(
            Long userId

    );

    public void processContact(ContactRequest req);

    public BusinessUser createUser( BusinessUser user);
}
