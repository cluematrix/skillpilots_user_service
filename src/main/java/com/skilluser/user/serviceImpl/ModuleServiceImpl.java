package com.skilluser.user.serviceImpl;

import com.skilluser.user.dto.ModulePermissionBulkDTO;
import com.skilluser.user.dto.ModulePermissionDTO;
import com.skilluser.user.dto.ModulePermissionGet;
import com.skilluser.user.dto.PermissionRequest;
import com.skilluser.user.model.*;
import com.skilluser.user.model.Module;
import com.skilluser.user.repository.*;
import com.skilluser.user.service.ModuleService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class ModuleServiceImpl implements ModuleService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final ModuleRepository moduleRepository;
    private final ModulePermissionRepository modulePermissionRepository;
    private final CustomRoleRepository customRoleRepository;
    private final CustomRolePermissionRepo customRolePermissionRepo;
    @Override
    public Module createModule(Module module) {
        return moduleRepository.save(module);
    }

    @Override
    public ModulePermission findByName(String name) {
        return null;// modulePermissionRepository.findByName(name);
    }

    public List<ModulePermissionDTO> addModulePermissions(ModulePermissionBulkDTO request) {
        Role role = roleRepository.findById(request.getRoleId())
                .orElseThrow(() -> new RuntimeException("Role not found"));

        List<ModulePermissionDTO> savedPermissions = new ArrayList<>();

        for (ModulePermissionDTO dto : request.getPermissions()) {
            Module module = moduleRepository.findById(dto.getModuleId())
                    .orElseThrow(() -> new RuntimeException("Module not found"));

            // Check if permission already exists
            ModulePermission permission = modulePermissionRepository
                    .findByRoleIdAndModuleId(role.getId(), module.getId())
                    .orElse(new ModulePermission());

            permission.setRole(role);
            permission.setModule(module);
            permission.setCanView(dto.isCanView());
            permission.setCanAdd(dto.isCanAdd());
            permission.setCanEdit(dto.isCanEdit());
            permission.setCanDelete(dto.isCanDelete());

            ModulePermission modulePermission = modulePermissionRepository.save(permission);
            ModulePermissionDTO modulePermissionDTO = getModulePermissionDTO(modulePermission);
            savedPermissions.add(modulePermissionDTO);
        }

        return savedPermissions;
    }

    @Override
    public List<ModulePermission> saveOrUpdatePermissions(
            Long roleId, Long collegeId, Long companyId, List<ModulePermissionDTO> permissionDTOs) {

        Role role = roleRepository.findById(roleId)
                .orElseThrow(() -> new RuntimeException("Role not found"));

        List<ModulePermission> existingPermissions;

        if (collegeId != null) {
            existingPermissions = modulePermissionRepository.findByRoleIdAndCollegeId(roleId, collegeId);
        } else if (companyId != null) {
            existingPermissions = modulePermissionRepository.findByRoleIdAndCompanyId(roleId, companyId);
        } else {
            // Global / admin scope
            existingPermissions = modulePermissionRepository
                    .findByRoleIdAndCollegeIdIsNullAndCompanyIdIsNull(roleId);
        }

        if (!existingPermissions.isEmpty()) {
            for (ModulePermission mp : existingPermissions) {
                permissionDTOs.stream()
                        .filter(dto -> dto.getModuleId().equals(mp.getModule().getId()))
                        .findFirst()
                        .ifPresent(dto -> {
                            mp.setCanView(dto.isCanView());
                            mp.setCanAdd(dto.isCanAdd());
                            mp.setCanEdit(dto.isCanEdit());
                            mp.setCanDelete(dto.isCanDelete());
                        });
            }
            return modulePermissionRepository.saveAll(existingPermissions);
        }

        // Create new
        List<ModulePermission> newPermissions = permissionDTOs.stream().map(dto -> {
            Module module = moduleRepository.findById(dto.getModuleId())
                    .orElseThrow(() -> new RuntimeException("Module not found"));

            ModulePermission mp = new ModulePermission();
            mp.setRole(role);
            mp.setModule(module);
            mp.setCanView(dto.isCanView());
            mp.setCanAdd(dto.isCanAdd());
            mp.setCanEdit(dto.isCanEdit());
            mp.setCanDelete(dto.isCanDelete());
            mp.setCollegeId(collegeId);
            mp.setCompanyId(companyId);
            mp.setDefault(collegeId == null && companyId == null && role.isDefaultRole());
            mp.setPath(module.getPath());
            return mp;
        }).collect(Collectors.toList());

        return modulePermissionRepository.saveAll(newPermissions);
    }

    private static ModulePermissionDTO getModulePermissionDTO(ModulePermission modulePermission) {
        ModulePermissionDTO modulePermissionDTO = new ModulePermissionDTO();
        modulePermissionDTO.setModuleId(modulePermission.getModule().getId());
        modulePermissionDTO.setServiceType(modulePermission.getModule().getServiceType());
        modulePermissionDTO.setRoleId(modulePermission.getRole().getId());
        modulePermissionDTO.setCanEdit(modulePermission.isCanEdit());
        modulePermissionDTO.setCanAdd(modulePermission.isCanAdd());
        modulePermissionDTO.setCanView(modulePermission.isCanView());
        modulePermissionDTO.setCanDelete(modulePermission.isCanDelete());
        return modulePermissionDTO;
    }

    @Override
    public List<ModulePermissionGet> getModulePermission(Long userId) {
        User user = userRepository.findById(userId).orElseThrow();

        System.out.println("User" + user);
        Role role = user.getRoles();
        ModulePermissionGet module = new ModulePermissionGet();
        ArrayList<ModulePermissionGet> list = new ArrayList<>();
        List<ModulePermission> permissions = modulePermissionRepository.findByRoleId(role.getId());
        for (ModulePermission m : permissions) {
            module.setName(m.getRole().getName());
            module.setModalName(m.getModule().getName());
            module.setCanView(m.isCanView());
            module.setCanEdit(m.isCanEdit());
            module.setCanAdd(m.isCanAdd());
            module.setCanDelete(m.isCanDelete());
            module.setPath(module.getPath());
            list.add(module);
        }

        return list;
    }

    @Override
    public List<ModulePermission> getPermissionsForRole(Long roleId, Long collegeId, Long companyId) {
//        List<ModulePermission> perms = modulePermissionRepository
//                .findByRoleIdAndCollegeIdOrCompanyId(roleId, collegeId, companyId);
//
//        if (perms.isEmpty()) {
//            perms = modulePermissionRepository.findByRoleIdAndIsDefaultTrue(roleId);
//        }
        List<ModulePermission> existingPermissions;
        if (collegeId != null) {
            existingPermissions = modulePermissionRepository.findByRoleIdAndCollegeId(roleId, collegeId);
        } else if (companyId != null) {
            existingPermissions = modulePermissionRepository.findByRoleIdAndCompanyId(roleId, companyId);
        } else {
            // Global / admin scope
            existingPermissions = modulePermissionRepository
                    .findByRoleIdAndCollegeIdIsNullAndCompanyIdIsNull(roleId);
        }
        return existingPermissions;
    }



    @Override
    public void setPermissions(Long roleId, Long collegeId, Long companyId, List<PermissionRequest> permissions) {
        Optional<Role> roleOpt = roleRepository.findById(roleId);

        if (roleOpt.isPresent()) {
            Role role = roleOpt.get();

            // Predefined Role ka case
            for (PermissionRequest req : permissions) {
                Module module = moduleRepository.findById(req.getModuleId())
                        .orElseThrow(() -> new RuntimeException("Module not found"));
                ModulePermission perm = modulePermissionRepository
                        .findByRoleIdAndModuleIdAndCollegeIdAndCompanyId(roleId, req.getModuleId(), collegeId, companyId)
                        .orElse(new ModulePermission());

                perm.setRole(role);
                perm.setModule(module);
                perm.setPath(req.getPath());
                perm.setCanView(req.isCanView());
                perm.setCanAdd(req.isCanAdd());
                perm.setCanEdit(req.isCanEdit());
                perm.setCanDelete(req.isCanDelete());
                perm.setCollegeId(collegeId);
                perm.setCompanyId(companyId);
                modulePermissionRepository.save(perm);
            }

        } else {
            // Custom Role ka case
            CustomRole customRole = customRoleRepository.findById(roleId)
                    .orElseThrow(() -> new RuntimeException("Custom role not found"));

            for (PermissionRequest req : permissions) {
                CustomRolePermission perm = customRolePermissionRepo
                        .findByCustomRoleIdAndModuleIdAndPath(customRole.getId(), req.getModuleId(), req.getPath())
                        .orElse(new CustomRolePermission());
                Module module = moduleRepository.findById(req.getModuleId())
                        .orElseThrow(() -> new RuntimeException("Module not found"));
                perm.setCustomRole(customRole);
                perm.setModule(module);
                perm.setPath(req.getPath());
                perm.setCanView(req.isCanView());
                perm.setCanAdd(req.isCanAdd());
                perm.setCanEdit(req.isCanEdit());
                perm.setCanDelete(req.isCanDelete());
                customRolePermissionRepo.save(perm);
            }
        }
    }

    @Override

    public Map<String, Object> getPermissionsForUser(Long userId) {
        Map<String, Object> response = new HashMap<>();
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User Not found"));
        List<Map<String, Object>> permsList = new ArrayList<>();

        if (user.getRoles() != null) { // Predefined role
            Long roleId = user.getRoles().getId();
            long id = (long) user.getCollegeId();  // int → Long

            Long collegeId = user.getCollegeId() != 0 ? id : null;
            Long companyId = user.getCompanyId() != null ? user.getCompanyId() : null;

            List<ModulePermission> permissions;

            if (collegeId != null) {
                permissions = modulePermissionRepository.findByRoleIdAndCollegeId(roleId, collegeId);
                if (permissions.isEmpty()) {
                    permissions = modulePermissionRepository.findByRoleIdAndCollegeIdIsNullAndCompanyIdIsNull(roleId);
                }
            } else if (companyId != null) {
                permissions = modulePermissionRepository.findByRoleIdAndCompanyId(roleId, companyId);
                if (permissions.isEmpty()) {
                    permissions = modulePermissionRepository.findByRoleIdAndCollegeIdIsNullAndCompanyIdIsNull(roleId);
                }
            } else {
                // fallback → admin default
                permissions = modulePermissionRepository.findByRoleIdAndCollegeIdIsNullAndCompanyIdIsNull(roleId);
            }

            for (ModulePermission p : permissions) {
                Map<String, Object> map = new HashMap<>();
                map.put("module", p.getModule().getName());
                map.put("path", p.getPath());
                map.put("canView", p.isCanView());
                map.put("canAdd", p.isCanAdd());
                map.put("canEdit", p.isCanEdit());
                map.put("canDelete", p.isCanDelete());
                map.put("isSidebar",p.getModule().isSidebar());
                permsList.add(map);
            }

            response.put("roleType", "PREDEFINED");
            response.put("roleName", user.getRoles().getName());
            response.put("permissions", permsList);

//        } else if (user.getCustomRole() != null) { // Custom role
//            List<CustomRolePermission> permissions =
//                    customRolePermissionRepository.findByCustomRoleId(user.getCustomRole().getId());
//
//            for (CustomRolePermission p : permissions) {
//                Map<String, Object> map = new HashMap<>();
//                map.put("module", p.getModule().getName());
//                map.put("path", p.getPath());
//                map.put("canView", p.isCanView());
//                map.put("canAdd", p.isCanAdd());
//                map.put("canEdit", p.isCanEdit());
//                map.put("canDelete", p.isCanDelete());
//                permsList.add(map);
//            }
//
//            response.put("roleType", "CUSTOM");
//            response.put("roleName", user.getCustomRole().getName());
//            response.put("permissions", permsList);
//        }
        }
            return response;
        }

}



