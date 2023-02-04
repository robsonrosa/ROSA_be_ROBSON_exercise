package com.ecore.roles.service.impl;

import com.ecore.roles.exception.InvalidArgumentException;
import com.ecore.roles.exception.ResourceExistsException;
import com.ecore.roles.exception.ResourceNotFoundException;
import com.ecore.roles.model.Membership;
import com.ecore.roles.model.Role;
import com.ecore.roles.repository.MembershipRepository;
import com.ecore.roles.service.MembershipsService;
import com.ecore.roles.service.RolesService;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

import static java.util.Objects.isNull;
import static java.util.Optional.ofNullable;

@RequiredArgsConstructor
@Service
class MembershipsServiceImpl implements MembershipsService {

    private final MembershipRepository repository;

    private final RolesService rolesService;

    @Override
    public Membership assignRoleToMembership(@NonNull final Membership membership) {

        final UUID userId = membership.getUserId();
        final UUID teamId = membership.getTeamId();
        final UUID roleId = ofNullable(membership.getRole()).map(Role::getId)
                .orElseThrow(() -> new InvalidArgumentException(Role.class));

        if (isNull(rolesService.getRole(roleId))) {
            throw new ResourceNotFoundException(Role.class, roleId);
        }

        if (repository.existsByUserIdAndTeamId(userId, teamId)) {
            throw new ResourceExistsException(Membership.class);
        }

        return repository.save(membership);
    }

    @Override
    public List<Membership> getMemberships(@NonNull final UUID roleId) {
        return repository.findByRoleId(roleId);
    }
}
