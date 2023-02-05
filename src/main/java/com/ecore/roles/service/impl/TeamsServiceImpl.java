package com.ecore.roles.service.impl;

import com.ecore.roles.client.TeamsClient;
import com.ecore.roles.client.model.Team;
import com.ecore.roles.exception.ResourceNotFoundException;
import com.ecore.roles.service.TeamsService;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

import static java.util.Optional.ofNullable;

@RequiredArgsConstructor
@Service
class TeamsServiceImpl implements TeamsService {

    private final TeamsClient client;

    @Cacheable("get-one-team")
    public Team getTeam(UUID id) {
        return ofNullable(client.getTeam(id).getBody())
                .orElseThrow(() -> new ResourceNotFoundException(Team.class, id));
    }

    @Cacheable("get-all-teams")
    public List<Team> getTeams() {
        return client.getTeams().getBody();
    }
}
