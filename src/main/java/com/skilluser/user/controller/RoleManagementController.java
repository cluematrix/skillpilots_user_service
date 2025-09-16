package com.skilluser.user.controller;

import com.skilluser.user.dto.ModulePermissionBulkDTO;
import com.skilluser.user.dto.ModulePermissionDTO;
import com.skilluser.user.dto.ModulePermissionGet;
import com.skilluser.user.model.Module;
import com.skilluser.user.model.ModulePermission;
import com.skilluser.user.model.Role;
import com.skilluser.user.service.ModuleService;
import com.skilluser.user.service.RoleService;
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


}
