package com.ecore.roles.service.impl;

import com.ecore.roles.client.UsersClient;
import com.ecore.roles.client.model.User;
import com.ecore.roles.exception.ResourceNotFoundException;
import org.jeasy.random.EasyRandom;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.UUID;

import static java.util.UUID.randomUUID;
import static java.util.stream.Collectors.toList;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;
import static org.springframework.http.HttpStatus.OK;

@ExtendWith(MockitoExtension.class)
class UsersServiceTest {

    @InjectMocks
    private UsersServiceImpl service;

    @Mock
    private UsersClient rest;

    private final EasyRandom generator = new EasyRandom();

    @Test
    void shouldGetUserWhenUserIdExists() {
        final User expected = generator.nextObject(User.class);

        when(rest.getUser(expected.getId()))
                .thenReturn(ResponseEntity
                        .status(OK)
                        .body(expected));

        final User actual = service.getUser(expected.getId());

        validateEquals(expected, actual);
    }

    @Test
    void shouldThrowsNotFoundExceptionWhenIdDoNotExists() {
        final UUID id = randomUUID();

        when(rest.getUser(id))
                .thenReturn(ResponseEntity
                        .status(OK)
                        .body(null));

        assertThrows(ResourceNotFoundException.class, () -> service.getUser(id));
    }

    @Test
    void shouldListAllUsers() {
        final List<User> expected = generator.objects(User.class, 5).collect(toList());

        when(rest.getUsers())
                .thenReturn(ResponseEntity
                        .status(OK)
                        .body(expected));

        final List<User> actual = service.getUsers();

        for (int i = 0; i < expected.size(); i++) {
            validateEquals(expected.get(i), actual.get(i));
        }
    }

    private static void validateEquals(final User expected, final User actual) {
        assertEquals(expected.getId(), actual.getId());
        assertEquals(expected.getLocation(), actual.getLocation());
        assertEquals(expected.getLastName(), actual.getLastName());
        assertEquals(expected.getFirstName(), actual.getFirstName());
        assertEquals(expected.getDisplayName(), actual.getDisplayName());
        assertEquals(expected.getAvatarUrl(), actual.getAvatarUrl());
    }

}
