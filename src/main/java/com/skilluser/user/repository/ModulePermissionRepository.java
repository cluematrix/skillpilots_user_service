package com.skilluser.user.repository;

import com.skilluser.user.model.ModulePermission;
import com.skilluser.user.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface ModulePermissionRepository extends JpaRepository<ModulePermission,Long> {
    Optional<ModulePermission> findByRoleIdAndModuleId(Long roleId, Long moduleId);

    List<ModulePermission> findByRoleId(Long id);

    List<ModulePermission> findByRole(Role role);


    // scoped queries
    List<ModulePermission> findByRoleAndCollegeId(Role role, Long collegeId);
    List<ModulePermission> findByRoleAndCompanyId(Role role, Long companyId);


    // default entries for role
    List<ModulePermission> findByRoleIdAndIsDefaultTrue(Long role);


    // fetch default entries without role scope (if needed)
    List<ModulePermission> findByIsDefaultTrue();


    // fetch scoped entries for a given role and module
    ModulePermission findByRoleAndModuleAndCollegeId(Role role, Module module, Long collegeId);
    ModulePermission findByRoleAndModuleAndCompanyId(Role role, Module module, Long companyId);
    ModulePermission findByRoleAndModuleAndIsDefaultTrue(Role role, Module module);

    @Query("SELECT mp FROM ModulePermission mp " +
            "WHERE mp.role.id = :roleId " +
            "AND (:collegeId IS NULL OR mp.collegeId = :collegeId) " +
            "AND (:companyId IS NULL OR mp.companyId = :companyId)")
    List<ModulePermission> findByRoleIdAndCollegeIdOrCompanyId(@Param("roleId") Long roleId,
                                                               @Param("collegeId") Long collegeId,
                                                               @Param("companyId") Long companyId);

    List<ModulePermission> findByRoleIdAndCollegeId(Long roleId, Long collegeId);

    List<ModulePermission> findByRoleIdAndCompanyId(Long roleId, Long companyId);

    List<ModulePermission> findByRoleIdAndCollegeIdIsNullAndCompanyIdIsNull(Long roleId);

    Optional<ModulePermission> findByRoleIdAndModuleIdAndCollegeIdAndCompanyId(Long roleId, Long moduleId, Long collegeId, Long companyId);
    // public ModulePermission findByName(String name);
}
