package com.skilluser.user.repository;

import com.skilluser.user.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface UserRepository extends JpaRepository<User,Long> {
    User findByEmail(String username);
<<<<<<< HEAD

    @Query("SELECT u FROM User u WHERE u.roles.id = :roleId AND (:departmentId IS NULL OR u.department = :departmentId)")
    List<User> findUsersByRoleAndDepartment(Long roleId, Long departmentId);

    List<User> findUsersByRoles_IdAndCollegeId(Long roleId, Long collegeId);

    @Query("SELECT u FROM User u WHERE u.roles.id = :roleId AND u.department = :departmentId")
    List<User> findHodByDepartment(Long roleId, Long departmentId);
=======
    User findByUsername(String username);
>>>>>>> b45dd3c14b5f4290140d72ecae28211640eb0035
}
