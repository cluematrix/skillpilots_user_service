package com.skilluser.user.controller;

import com.skilluser.user.dto.ModulePermissionBulkDTO;
import com.skilluser.user.dto.ModulePermissionDTO;
import com.skilluser.user.dto.ModulePermissionGet;
import com.skilluser.user.model.Module;
import com.skilluser.user.model.ModulePermission;
import com.skilluser.user.model.Role;
import com.skilluser.user.repository.RoleRepository;
import com.skilluser.user.service.ModuleService;
import com.skilluser.user.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("api/v1/users")
public class RoleManagementController {


    private final RoleService roleService;
    private final ModuleService moduleService;
    @Autowired
    private RoleRepository roleRepository;

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
    @GetMapping("/role/{name}")
    public Role findRole(@PathVariable String name){
        return roleService.findByName(name);
    }

    @PostMapping("/role/model")
    public Module createModule(@RequestBody Module module)
    {
        return  moduleService.createModule(module);
    }

    @PostMapping("/role/permissions")
    public ResponseEntity<?> assignModulePermissions(@RequestBody ModulePermissionBulkDTO request) {
        try {
            return ResponseEntity.status(200).body(Map.of(
                    "data",moduleService.addModulePermissions(request),"msg","Permission give successfully"));
        } catch (RuntimeException e) {
            return ResponseEntity.status(500).body(Map.of("msg",e.getMessage()));
        }

    }

    @GetMapping("/role/permissions/{name}")
    public ResponseEntity<?> getPermission(@PathVariable String name ){

        ModulePermission byName = moduleService.findByName(name);
        return ResponseEntity.status(200).body(byName);
    }

    @GetMapping("/role/sidebar/{userId}")
    public ResponseEntity<?> getSidebar(@PathVariable Long userId) {
        System.out.println("User"+ userId);
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
//
//    @GetMapping("/id/{id}")
//    public Role findRoleById(@PathVariable("id") Long id){
//        return roleRepository.findById(id)
//                .orElseThrow(() -> new RuntimeException("Role not found for this id: "+id));
//    }

}
