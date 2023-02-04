package com.ecore.roles.service.impl;

import com.ecore.roles.client.model.Team;
import com.ecore.roles.client.model.User;
import com.ecore.roles.exception.InvalidArgumentException;
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

import java.util.Arrays;
import java.util.UUID;

import static java.lang.String.format;
import static java.util.UUID.randomUUID;
import static java.util.stream.Collectors.toList;
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
        final User expectedUser = generator.nextObject(User.class);
        final Team expectedTeam = generator.nextObject(Team.class)
                .toBuilder()
                .teamMemberIds(Arrays.asList(userId))
                .build();
        final Membership expected = model.toBuilder()
                .id(randomUUID()).build();

        when(rolesService.getRole(roleId)).thenReturn(expectedRole);
        when(membershipRepository.existsByUserIdAndTeamId(userId, teamId)).thenReturn(false);
        when(usersService.getUser(userId)).thenReturn(expectedUser);
        when(teamsService.getTeam(teamId)).thenReturn(expectedTeam);
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
    public void shouldFailToCreateMembershipWhenItHasInvalidRole() {
        final Membership model = build();
        final UUID roleId = model.getRole().getId();

        when(rolesService.getRole(roleId)).thenThrow(new ResourceNotFoundException(Role.class, roleId));

        final ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class,
                () -> membershipsService.assignRoleToMembership(model));

        assertEquals(format("Role %s not found", roleId), exception.getMessage());
        verify(usersService, never()).getUser(any());
        verify(teamsService, never()).getTeam(any());
        verify(membershipRepository, never()).save(any());

        // make sure search role occurs first, assuming is faster search for one primary key
        verify(rolesService, times(1)).getRole(roleId);
        verify(membershipRepository, never()).existsByUserIdAndTeamId(any(), any());

        // make sure the local access occurs first (assuming that the database access is faster)
        verify(usersService, never()).getUser(any());
        verify(teamsService, never()).getTeam(any());
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

        final ResourceExistsException exception = assertThrows(ResourceExistsException.class,
                () -> membershipsService.assignRoleToMembership(model));

        assertEquals("Membership already exists", exception.getMessage());
        verify(membershipRepository, never()).save(any());

        // make sure search role occurs first, assuming is faster search for one primary key
        verify(rolesService, times(1)).getRole(roleId);

        // make sure the local access occurs first (assuming that the database access is faster)
        verify(usersService, never()).getUser(any());
        verify(teamsService, never()).getTeam(any());
    }

    @Test
    public void shouldFailToCreateMembershipWhenUserDoesNotExist() {
        final Membership model = build();
        final UUID userId = model.getUserId();
        final UUID teamId = model.getTeamId();
        final UUID roleId = model.getRole().getId();

        final Role expectedRole = generator.nextObject(Role.class);
        final Team expectedTeam = generator.nextObject(Team.class);
        final Membership expected = model.toBuilder()
                .id(randomUUID()).build();

        when(rolesService.getRole(roleId)).thenReturn(expectedRole);
        when(membershipRepository.existsByUserIdAndTeamId(userId, teamId)).thenReturn(false);
        when(usersService.getUser(userId)).thenThrow(new ResourceNotFoundException(User.class, userId));
        when(teamsService.getTeam(teamId)).thenReturn(expectedTeam);

        final ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class,
                () -> membershipsService.assignRoleToMembership(model));

        final Membership actual = membershipsService.assignRoleToMembership(model);

        assertEquals(format("User %s not found", userId), exception.getMessage());
        assertNotNull(actual);
        assertEquals(actual, expected);

        // make sure the local access occurs first (assuming that the database access is faster)
        verify(rolesService, times(1)).getRole(roleId);
        verify(membershipRepository, times(1)).existsByUserIdAndTeamId(userId, teamId);
    }

    @Test
    public void shouldFailToCreateMembershipWhenTeamDoesNotExist() {
        final Membership model = build();
        final UUID userId = model.getUserId();
        final UUID teamId = model.getTeamId();
        final UUID roleId = model.getRole().getId();

        final Role expectedRole = generator.nextObject(Role.class);
        final User expectedUser = generator.nextObject(User.class);
        final Membership expected = model.toBuilder()
                .id(randomUUID()).build();

        when(rolesService.getRole(roleId)).thenReturn(expectedRole);
        when(membershipRepository.existsByUserIdAndTeamId(userId, teamId)).thenReturn(false);
        when(usersService.getUser(userId)).thenReturn(expectedUser);
        when(teamsService.getTeam(teamId)).thenThrow(new ResourceNotFoundException(Team.class, teamId));

        final ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class,
                () -> membershipsService.assignRoleToMembership(model));

        final Membership actual = membershipsService.assignRoleToMembership(model);

        assertEquals(format("User %s not found", userId), exception.getMessage());
        assertNotNull(actual);
        assertEquals(actual, expected);

        // make sure the local access occurs first (assuming that the database access is faster)
        verify(rolesService, times(1)).getRole(roleId);
        verify(membershipRepository, times(1)).existsByUserIdAndTeamId(userId, teamId);
    }

    @Test
    public void shouldFailToCreateMembershipWhenUserDoesNotBelongTheTeam() {
        final Membership model = build();
        final UUID userId = model.getUserId();
        final UUID teamId = model.getTeamId();
        final UUID roleId = model.getRole().getId();

        final Role expectedRole = generator.nextObject(Role.class);
        final User expectedUser = generator.nextObject(User.class);
        final Team expectedTeam = generator.nextObject(Team.class)
                .toBuilder()
                .teamMemberIds(generator.objects(UUID.class, 10).collect(toList()))
                .build();
        final Membership expected = model.toBuilder()
                .id(randomUUID()).build();

        when(rolesService.getRole(roleId)).thenReturn(expectedRole);
        when(membershipRepository.existsByUserIdAndTeamId(userId, teamId)).thenReturn(false);
        when(usersService.getUser(userId)).thenReturn(expectedUser);
        when(teamsService.getTeam(teamId)).thenReturn(expectedTeam);

        final InvalidArgumentException exception = assertThrows(InvalidArgumentException.class,
                () -> membershipsService.assignRoleToMembership(model));

        final Membership actual = membershipsService.assignRoleToMembership(model);

        assertEquals("Invalid 'Membership' object. The provided user doesn't belong to the provided team.",
                exception.getMessage());
        assertNotNull(actual);
        assertEquals(actual, expected);

        // make sure the local access occurs first (assuming that the database access is faster)
        verify(rolesService, times(1)).getRole(roleId);
        verify(membershipRepository, times(1)).existsByUserIdAndTeamId(userId, teamId);
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
