package com.ecore.roles.mapper;

import com.ecore.roles.client.model.Team;
import com.ecore.roles.web.dto.TeamDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class TeamMapper extends AbstractMapper<Team, TeamDto> {

    private final ObjectMapper mapper;

    @Override
    protected ObjectMapper getMapper() {
        return mapper;
    }
}
