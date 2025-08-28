package com.skilluser.user.service;

import com.skilluser.user.model.Role;

public interface RoleService {

    public Role createRole(Role role);

    public Role findByName(String name);
}
