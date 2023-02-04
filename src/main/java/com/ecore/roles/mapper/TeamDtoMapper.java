package com.ecore.roles.mapper;

import com.ecore.roles.client.model.Team;
import com.ecore.roles.web.dto.TeamDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import static java.util.Optional.ofNullable;

@RequiredArgsConstructor
@Component
public class TeamDtoMapper implements Mapper<Team, TeamDto> {

    private final ObjectMapper mapper;

    @Override
    public TeamDto apply(final Team user) {
        return ofNullable(user)
                .map(this::convert)
                .orElse(null);
    }

    private TeamDto convert(final Team user) {
        return mapper.convertValue(user, TeamDto.class);
    }
}
