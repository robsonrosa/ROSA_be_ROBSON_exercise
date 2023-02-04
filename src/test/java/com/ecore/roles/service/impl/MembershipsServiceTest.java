package com.ecore.roles.service.impl;

import com.ecore.roles.exception.ResourceExistsException;
import com.ecore.roles.exception.ResourceNotFoundException;
import com.ecore.roles.model.Membership;
import com.ecore.roles.model.Role;
import com.ecore.roles.repository.MembershipRepository;
import com.ecore.roles.service.RolesService;
import com.ecore.roles.service.TeamsService;
import com.ecore.roles.service.UsersService;
import org.jeasy.random.EasyRandom;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.UUID;

import static java.lang.String.format;
import static java.util.UUID.randomUUID;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MembershipsServiceTest {

    private static final EasyRandom generator = new EasyRandom();

    @InjectMocks
    private MembershipsServiceImpl membershipsService;

    @Mock
    private MembershipRepository membershipRepository;

    @Mock
    private RolesService rolesService;

    @Mock
    private UsersService usersService;

    @Mock
    private TeamsService teamsService;

    @Test
    public void shouldCreateMembership() {
        final Membership model = build();
        final UUID userId = model.getUserId();
        final UUID teamId = model.getTeamId();
        final UUID roleId = model.getRole().getId();

        final Role expectedRole = generator.nextObject(Role.class);
        final Membership expected = model.toBuilder()
                .id(randomUUID()).build();

        when(rolesService.getRole(roleId)).thenReturn(expectedRole);
        when(membershipRepository.existsByUserIdAndTeamId(userId, teamId)).thenReturn(false);
        when(membershipRepository.save(model)).thenReturn(expected);

        final Membership actual = membershipsService.assignRoleToMembership(model);

        assertNotNull(actual);
        assertEquals(actual, expected);
        verify(rolesService, times(1)).getRole(roleId);
    }

    @Test
    public void shouldFailToCreateMembershipWhenMembershipsIsNull() {
        assertThrows(NullPointerException.class,
                () -> membershipsService.assignRoleToMembership(null));
    }

    @Test
    public void shouldFailToCreateMembershipWhenRoleIsNull() {
        final Membership model = build();
        final UUID roleId = model.getRole().getId();

        when(rolesService.getRole(roleId)).thenReturn(null);

        final ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class,
                () -> membershipsService.assignRoleToMembership(model));

        assertEquals(format("Role %s not found", roleId), exception.getMessage());
        verify(usersService, never()).getUser(any());
        verify(teamsService, never()).getTeam(any());
        verify(membershipRepository, never()).save(any());

        // make sure search role occurs first, because it's faster search for one primary key than a
        // composite index
        verify(rolesService, times(1)).getRole(roleId);
        verify(membershipRepository, never()).existsByUserIdAndTeamId(any(), any());
    }

    @Test
    public void shouldFailToCreateMembershipWhenItHasInvalidRole() {
        final Membership model = build();
        final UUID roleId = model.getRole().getId();

        when(rolesService.getRole(roleId)).thenThrow(ResourceNotFoundException.class);

        assertThrows(ResourceNotFoundException.class,
                () -> membershipsService.assignRoleToMembership(model));

        verify(usersService, never()).getUser(any());
        verify(teamsService, never()).getTeam(any());
        verify(membershipRepository, never()).save(any());

        // make sure search role occurs first, because it's faster search for one primary key than a
        // composite index
        verify(rolesService, times(1)).getRole(roleId);
        verify(membershipRepository, never()).existsByUserIdAndTeamId(any(), any());
    }

    @Test
    public void shouldFailToCreateMembershipWhenItExists() {
        final Membership model = build();
        final UUID userId = model.getUserId();
        final UUID teamId = model.getTeamId();
        final UUID roleId = model.getRole().getId();

        final Role expectedRole = generator.nextObject(Role.class);

        when(rolesService.getRole(roleId)).thenReturn(expectedRole);
        when(membershipRepository.existsByUserIdAndTeamId(userId, teamId)).thenReturn(true);

        ResourceExistsException exception = assertThrows(ResourceExistsException.class,
                () -> membershipsService.assignRoleToMembership(model));

        assertEquals("Membership already exists", exception.getMessage());
        verify(usersService, never()).getUser(any());
        verify(teamsService, never()).getTeam(any());
        verify(membershipRepository, never()).save(any());

        // make sure search role occurs first, because it's faster search for one primary key than a
        // composite index
        verify(rolesService, times(1)).getRole(roleId);
    }

    @Test
    public void shouldFailToGetMembershipsWhenRoleIdIsNull() {
        assertThrows(NullPointerException.class,
                () -> membershipsService.getMemberships(null));
    }

    private Membership build() {
        return generator.nextObject(Membership.class)
                .toBuilder()
                .id(null)
                .build();
    }

}
