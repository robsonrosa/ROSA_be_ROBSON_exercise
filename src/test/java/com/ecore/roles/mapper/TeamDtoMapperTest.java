package com.ecore.roles.mapper;

import com.ecore.roles.client.model.Team;
import com.ecore.roles.web.dto.TeamDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.jeasy.random.EasyRandom;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static java.util.stream.Collectors.toList;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class TeamDtoMapperTest {

    @Autowired
    private TeamDtoMapper mapper;

    private final EasyRandom generator = new EasyRandom();

    private Team team;

    @BeforeEach
    public void setup() {
        team = generator.nextObject(Team.class);
    }

    @Test
    public void shouldReturnNullWhenConvertingNull() {
        assertNull(mapper.apply(null));
    }

    @Test
    public void shouldReturnEmptyListWhenConvertingNullList() {
        assertTrue(mapper.applyList(null).isEmpty());
    }

    @Test
    public void shouldReturnDtoWithSameValues() {
        final TeamDto dto = mapper.apply(team);
        validateEquals(team, dto);
    }

    @Test
    public void shouldReturnDtoListWithSameValues() {
        final List<Team> entityList = generator.objects(Team.class, 5).collect(toList());
        final List<TeamDto> dtoList = mapper.applyList(entityList);
        for (int i = 0; i < entityList.size(); i++) {
            validateEquals(entityList.get(i), dtoList.get(i));
        }
    }

    private void validateEquals(final Team team, final TeamDto dto) {
        assertEquals(team.getId(), dto.getId());
        assertEquals(team.getTeamLeadId(), dto.getTeamLeadId());
        assertEquals(team.getName(), dto.getName());
        assertEquals(stringify(team.getTeamMemberIds()), stringify(dto.getTeamMemberIds()));
    }

    @SneakyThrows
    private <T> String stringify(final List<T> list) {
        return new ObjectMapper().writeValueAsString(list);
    }

}
