package com.example.shopping_app.service;



import com.example.shopping_app.entity.Role;

import java.util.List;

public interface RoleService {

    Role getRoleById(long id);

    List<Role> getAllRole();
}
