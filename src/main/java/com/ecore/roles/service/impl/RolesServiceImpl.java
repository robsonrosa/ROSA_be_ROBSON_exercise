package com.ecore.roles.service.impl;

import com.ecore.roles.exception.ResourceExistsException;
import com.ecore.roles.exception.ResourceNotFoundException;
import com.ecore.roles.model.Role;
import com.ecore.roles.repository.RoleRepository;
import com.ecore.roles.service.RolesService;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

import static java.util.Objects.nonNull;

@RequiredArgsConstructor
@Service
class RolesServiceImpl implements RolesService {

    private final RoleRepository roleRepository;

    @Override
    public Role createRole(@NonNull final Role role) {
        if (nonNull(role.getId())) {
            throw new ResourceExistsException(Role.class);
        }

        if (roleRepository.existsByName(role.getName())) {
            throw new ResourceExistsException(Role.class);
        }
        return roleRepository.save(role);
    }

    @Override
    public Role getRole(@NonNull final UUID id) {
        return roleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(Role.class, id));
    }

    @Override
    public List<Role> getRoles() {
        return roleRepository.findAll();
    }

}
