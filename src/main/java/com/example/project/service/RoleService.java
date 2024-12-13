
package com.example.project.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.example.project.model.Role;
import com.example.project.repository.RoleRepository;

@Service
public class RoleService {
    @Autowired
    private RoleRepository roleRepository;

    public Role findByName(String name) {
        return roleRepository.findByName(String.valueOf(Role.RoleName.valueOf(name)));
    }

    public Role save(Role role) {
        return roleRepository.save(role);
    }
}
