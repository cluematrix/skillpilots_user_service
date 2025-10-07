package com.skilluser.user.controller;

import com.skilluser.user.dto.ModulePermissionBulkDTO;
import com.skilluser.user.dto.ModulePermissionDTO;
import com.skilluser.user.dto.ModulePermissionGet;
import com.skilluser.user.dto.PermissionSetRequest;
import com.skilluser.user.model.CustomRole;
import com.skilluser.user.model.Module;
import com.skilluser.user.model.ModulePermission;
import com.skilluser.user.model.Role;
import com.skilluser.user.service.ModuleService;
import com.skilluser.user.service.RoleService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

// shrunkhal 8/sept
@RestController
@RequestMapping("api/v1/users")
public class RoleManagementController {


    private final RoleService roleService;
    private final ModuleService moduleService;

    public RoleManagementController(RoleService roleService, ModuleService moduleService) {
        this.roleService = roleService;
        this.moduleService = moduleService;
    }

    @PostMapping  // add role   shrunkhal 08/sep
    public ResponseEntity<?> createRole(@RequestBody Role role) {
        try {
            Role savedRole = roleService.createRole(role);
            return ResponseEntity.ok(savedRole);
        } catch (IllegalArgumentException e) {
            Map<String, String> response = new HashMap<>();
            response.put("error", e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    @GetMapping("/role/{name}")   // get role   shrunkhal 08/sep
    public Role findRole(@PathVariable String name) {
        return roleService.findByName(name);
    }

    @PostMapping("/role/model")
    public Module createModule(@RequestBody Module module) {
        return moduleService.createModule(module);
    }

    @PostMapping("/role/permissions") // get permission   shrunkhal 08/sep
    public ResponseEntity<?> assignModulePermissions(@RequestBody ModulePermissionBulkDTO request) {
        try {
            return ResponseEntity.status(200).body(Map.of(
                    "data", moduleService.addModulePermissions(request), "msg", "Permission give successfully"));
        } catch (RuntimeException e) {
            return ResponseEntity.status(500).body(Map.of("msg", e.getMessage()));
        }

    }

    @GetMapping("/role/permissions/{name}")
    public ResponseEntity<?> getPermission(@PathVariable String name) {

        ModulePermission byName = moduleService.findByName(name);
        return ResponseEntity.status(200).body(byName);
    }

    @GetMapping("/role/sidebar/{userId}")
    public ResponseEntity<?> getSidebar(@PathVariable Long userId) {
        System.out.println("User" + userId);
        List<ModulePermissionGet> modulePermission = moduleService.getModulePermission(userId);

        return ResponseEntity.ok(modulePermission);
    }

    @DeleteMapping("role/delete/{id}")
    public ResponseEntity<String> deleteRole(@PathVariable Long id) {
        try {
            roleService.deleteRole(id);
            return ResponseEntity.ok("Role deleted successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Role not found with id: " + id);
        }
    }

    @PutMapping("/role/{id}")
    public ResponseEntity<Role> updateRole(@PathVariable Long id, @RequestBody Role updatedRole) {
        Role role = roleService.updateRole(id, updatedRole);
        return ResponseEntity.ok(role);
    }

    @PostMapping("/save")
    public List<ModulePermission> saveOrUpdatePermissions(
            @RequestParam Long roleId,
            @RequestParam(required = false) Long collegeId,
            @RequestParam(required = false) Long companyId,
            @RequestBody List<ModulePermissionDTO> permissions) {

        return moduleService.saveOrUpdatePermissions(roleId, collegeId, companyId, permissions);
    }

    @GetMapping("/permissions")
    public List<ModulePermission> getPermissions(
            @RequestParam Long roleId,
            @RequestParam(required = false) Long collegeId,
            @RequestParam(required = false) Long companyId) {

        return moduleService.getPermissionsForRole(roleId, collegeId, companyId);
    }

    @PostMapping("/custom_role")
    public ResponseEntity<?> createCustomRole(@RequestBody CustomRole customRole) {
        return ResponseEntity.status(200).body(roleService.createCustomRole(customRole));
    }

    @PostMapping("/set")
    public ResponseEntity<?> setPermissions(@RequestBody PermissionSetRequest request) {
        moduleService.setPermissions(
                request.getRoleId(),
                request.getCollegeId(),
                request.getCompanyId(),
                request.getPermissions()
        );
        return ResponseEntity.ok("Permissions updated successfully!");
    }

    @GetMapping("/me/{userId}")
    public ResponseEntity<?> getMyPermissions(@PathVariable Long userId) {

        Map<String, Object> permissions = moduleService.getPermissionsForUser(userId);
        return ResponseEntity.ok(permissions);
    }
}
