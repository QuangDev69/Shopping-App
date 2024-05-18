package com.example.shopping_app.controller;

import com.example.shopping_app.entity.Role;
import com.example.shopping_app.service.RoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("${api.prefix}/roles")
public class RoleController {
    private final RoleService roleService;

    @GetMapping("")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<?> getAllRole() {
        List<Role> roles = roleService.getAllRole();
        return ResponseEntity.ok(roles);
    }


}
