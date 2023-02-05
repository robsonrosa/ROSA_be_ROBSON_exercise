package com.ecore.roles.service.impl;

import com.ecore.roles.exception.ResourceExistsException;
import com.ecore.roles.exception.ResourceNotFoundException;
import com.ecore.roles.model.Role;
import com.ecore.roles.repository.RoleRepository;
import org.jeasy.random.EasyRandom;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.UUID;

import static com.ecore.roles.utils.TestData.RoleEnum.DEVELOPER;
import static java.lang.String.format;
import static java.util.Optional.empty;
import static java.util.Optional.of;
import static java.util.UUID.randomUUID;
import static java.util.stream.Collectors.toList;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RolesServiceTest {

    private static final EasyRandom generator = new EasyRandom();

    @InjectMocks
    private RolesServiceImpl service;

    @Mock
    private RoleRepository repository;

    @Test
    public void shouldFailToCreateRoleWhenRoleIsNull() {
        assertThrows(NullPointerException.class,
                () -> service.createRole(null));
    }

    @Test
    public void shouldFailToCreateRoleWhenAlreadyHasId() {
        final Role model = generator.nextObject(Role.class);

        try {
            service.createRole(model);
        } catch (ResourceExistsException exception) {
            verifyNoInteractions(repository);
            return;
        }

        fail("Should throw InvalidArgumentException");
    }

    @Test
    public void shouldFailToCreateRoleWhenRoleNameExists() {
        final Role model = buildWithName(DEVELOPER.getName());

        when(repository.existsByName(model.getName())).thenReturn(true);

        try {
            service.createRole(model);
        } catch (ResourceExistsException exception) {
            verify(repository, never()).save(any());
            return;
        }

        fail("Should throw InvalidArgumentException");
    }

    @Test
    public void shouldCreateRole() {
        final Role model = build();
        final Role expected = model.toBuilder()
                .id(randomUUID()).build();

        when(repository.existsByName(model.getName())).thenReturn(false);
        when(repository.save(model)).thenReturn(expected);

        final Role actual = service.createRole(model);
        assertEquals(expected, actual);

    }

    @Test
    public void shouldReturnRoleWhenRoleIdExists() {
        final UUID id = randomUUID();
        final Role expected = generator.nextObject(Role.class);

        when(repository.findById(id)).thenReturn(of(expected));

        final Role actual = service.getRole(id);
        assertEquals(expected, actual);
    }

    @Test
    public void shouldFailToGetRoleWhenRoleIdDoesNotExist() {
        final UUID id = randomUUID();

        when(repository.findById(any())).thenReturn(empty());

        final ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class,
                () -> service.getRole(id));

        assertEquals(format("Role %s not found", id), exception.getMessage());
    }

    @Test
    public void shouldReturnAllRoles() {
        final List<Role> expected = generator.objects(Role.class, 10).collect(toList());

        when(repository.findAll()).thenReturn(expected);

        final List<Role> actual = service.getRoles();

        assertEquals(expected, actual);
    }

    private Role build() {
        return generator.nextObject(Role.class)
                .toBuilder()
                .id(null)
                .build();
    }

    private Role buildWithName(final String name) {
        return build()
                .toBuilder()
                .name(name)
                .build();
    }
}
