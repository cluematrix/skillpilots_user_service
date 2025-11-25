package com.skilluser.user.repository;

import com.skilluser.user.enums.AuthProviderType;
import com.skilluser.user.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
<<<<<<< HEAD
import java.util.Optional;

=======
>>>>>>> cb7b8de00c0cc80532905ecb6387a299f12b6687
@Repository
public interface UserRepository extends JpaRepository<User,Long> {
    User findByEmail(String username);

    @Query("SELECT u FROM User u WHERE u.roles.id = :roleId AND (:departmentId IS NULL OR u.department = :departmentId)")
    List<User> findUsersByRoleAndDepartment(Long roleId, Long departmentId);

    List<User> findUsersByRoles_IdAndCollegeId(Long roleId, Long collegeId);

    @Query("SELECT u FROM User u WHERE u.roles.id = :roleId AND u.department = :departmentId")
    List<User> findHodByDepartment(Long roleId, Long departmentId);
    User findByUsername(String username);


    List<User> findByRoles_Name(String roleName);

    List<User> findByRoles_NameIn(List<String> roleNames);

    List<User> findByRoles_NameInAndCollegeId(List<String> roleNames, Long collegeId);

    List<User> findByRoles_NameInAndCompanyId(List<String> roleNames, Long companyId);

    List<User> findByRoles_NameInAndCollegeIdAndDepartment(List<String> roleNames, Long collegeId, Long deptId);

<<<<<<< HEAD
    Optional<User> findByProviderIdAndProviderType(String providerId, AuthProviderType providerType);
=======

>>>>>>> cb7b8de00c0cc80532905ecb6387a299f12b6687

}
