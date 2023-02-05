package com.ecore.roles.service.impl;

import com.ecore.roles.client.model.Team;
import com.ecore.roles.client.model.User;
import com.ecore.roles.exception.InvalidArgumentException;
import com.ecore.roles.exception.ResourceExistsException;
import com.ecore.roles.model.Membership;
import com.ecore.roles.model.Role;
import com.ecore.roles.repository.MembershipRepository;
import com.ecore.roles.service.MembershipsService;
import com.ecore.roles.service.RolesService;
import com.ecore.roles.service.TeamsService;
import com.ecore.roles.service.UsersService;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

import static java.util.Objects.nonNull;
import static java.util.Optional.ofNullable;

@RequiredArgsConstructor
@Service
class MembershipsServiceImpl implements MembershipsService {

    private final MembershipRepository repository;

    private final RolesService rolesService;

    private final UsersService usersService;

    private final TeamsService teamsService;

    @Override
    public Membership assignRoleToMembership(@NonNull final Membership membership) {

        final UUID userId = membership.getUserId();
        final UUID teamId = membership.getTeamId();
        final UUID roleId = ofNullable(membership.getRole()).map(Role::getId)
                .orElseThrow(() -> new InvalidArgumentException(Role.class));

        if (nonNull(membership.getId())) {
            throw new ResourceExistsException(Membership.class);
        }

        rolesService.getRole(roleId);

        if (repository.existsByUserIdAndTeamId(userId, teamId)) {
            throw new ResourceExistsException(Membership.class);
        }

        final User user = usersService.getUser(userId);
        final Team team = teamsService.getTeam(teamId);

        // TODO: assume that is valid if the user is the team's lead instead a member
        if (!team.getTeamMemberIds().contains(user.getId())) {
            throw new InvalidArgumentException(Membership.class,
                    "The provided user doesn't belong to the provided team");
        }

        return repository.save(membership);
    }

    @Override
    public List<Membership> getMemberships(@NonNull final UUID roleId) {
        // TODO: validate if roleId exists
        return repository.findByRoleId(roleId);
    }
}
