package com.ecore.roles.mapper;

import com.ecore.roles.client.model.Team;
import com.ecore.roles.web.dto.TeamDto;
import org.jeasy.random.EasyRandom;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static java.util.stream.Collectors.toList;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class TeamMapperTest {

    @Autowired
    private TeamMapper mapper;

    private final EasyRandom generator = new EasyRandom();

    private TeamDto dto;

    private Team model;

    @BeforeEach
    public void setup() {
        dto = generator.nextObject(TeamDto.class);
        model = generator.nextObject(Team.class);
    }

    @Test
    public void shouldReturnNullWhenConvertingFromNullModel() {
        assertNull(mapper.fromModel(null));
    }

    @Test
    public void shouldReturnDtoWithSameValuesFromModel() {
        final TeamDto dto = mapper.fromModel(model);
        validateEquals(model, dto);
    }

    @Test
    public void shouldReturnEmptyListWhenConvertingFromNullModelList() {
        assertTrue(mapper.fromModelList(null).isEmpty());
    }

    @Test
    public void shouldReturnDtoListWithSameValuesFromModelList() {
        final List<Team> modelList = generator.objects(Team.class, 5).collect(toList());
        final List<TeamDto> dtoList = mapper.fromModelList(modelList);
        for (int i = 0; i < modelList.size(); i++) {
            validateEquals(modelList.get(i), dtoList.get(i));
        }
    }

    @Test
    public void shouldReturnNullWhenConvertingFromNullDto() {
        assertNull(mapper.fromDto(null));
    }

    @Test
    public void shouldReturnModelWithSameValuesFromDto() {
        final Team model = mapper.fromDto(dto);
        validateEquals(model, dto);
    }

    @Test
    public void shouldReturnEmptyListWhenConvertingFromNullDtoList() {
        assertTrue(mapper.fromDtoList(null).isEmpty());
    }

    @Test
    public void shouldReturnModelListWithSameValuesFromDtoList() {
        final List<TeamDto> dtoList = generator.objects(TeamDto.class, 5).collect(toList());
        final List<Team> modelList = mapper.fromDtoList(dtoList);
        for (int i = 0; i < modelList.size(); i++) {
            validateEquals(modelList.get(i), dtoList.get(i));
        }
    }

    private void validateEquals(final Team team, final TeamDto dto) {
        assertEquals(team.getId(), dto.getId());
        assertEquals(team.getName(), dto.getName());
        assertEquals(team.getTeamLeadId(), dto.getTeamLeadId());
        assertEquals(team.getTeamMemberIds(), dto.getTeamMemberIds());
    }

}
