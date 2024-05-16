package com.example.shopping_app.service.impl;

import com.example.shopping_app.entity.Role;
import com.example.shopping_app.repository.RoleRepository;
import com.example.shopping_app.service.RoleService;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RoleServiceImpl implements RoleService {
    private RoleRepository roleRepository;
    @Override
    public Role getRoleById(long id) {
        return null;
    }

    @Override
    public List<Role> getAllRole() {
        return roleRepository.findAll();
    }
}
