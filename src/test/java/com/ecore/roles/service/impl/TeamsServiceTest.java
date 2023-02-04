package com.ecore.roles.service.impl;

import com.ecore.roles.client.TeamsClient;
import com.ecore.roles.client.model.Team;
import com.ecore.roles.exception.ResourceNotFoundException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
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
class TeamsServiceTest {

    @InjectMocks
    private TeamsServiceImpl service;

    @Mock
    private TeamsClient rest;

    private final EasyRandom generator = new EasyRandom();

    @Test
    void shouldGetTeamWhenTeamIdExists() {
        final Team expected = generator.nextObject(Team.class);

        when(rest.getTeam(expected.getId()))
                .thenReturn(ResponseEntity
                        .status(OK)
                        .body(expected));

        final Team actual = service.getTeam(expected.getId());

        validateEquals(expected, actual);
    }

    @Test
    void shouldThrowsNotFoundExceptionWhenIdDoNotExists() {
        final UUID id = randomUUID();

        when(rest.getTeam(id))
                .thenReturn(ResponseEntity
                        .status(OK)
                        .body(null));

        assertThrows(ResourceNotFoundException.class, () -> service.getTeam(id));
    }

    @Test
    void shouldListAllTeams() {
        final List<Team> expected = generator.objects(Team.class, 5).collect(toList());

        when(rest.getTeams())
                .thenReturn(ResponseEntity
                        .status(OK)
                        .body(expected));

        final List<Team> actual = service.getTeams();

        for (int i = 0; i < expected.size(); i++) {
            validateEquals(expected.get(i), actual.get(i));
        }
    }

    private void validateEquals(final Team expected, final Team actual) {
        assertEquals(expected.getId(), actual.getId());
        assertEquals(expected.getName(), actual.getName());
        assertEquals(expected.getTeamLeadId(), actual.getTeamLeadId());
        assertEquals(stringify(expected.getTeamMemberIds()), stringify(actual.getTeamMemberIds()));
    }

    @SneakyThrows
    private <T> String stringify(final List<T> list) {
        return new ObjectMapper().writeValueAsString(list);
    }
}
