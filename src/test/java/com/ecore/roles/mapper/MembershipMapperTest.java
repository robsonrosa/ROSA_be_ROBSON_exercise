package com.ecore.roles.mapper;

import com.ecore.roles.model.Membership;
import com.ecore.roles.web.dto.MembershipDto;
import org.jeasy.random.EasyRandom;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static java.util.stream.Collectors.toList;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class MembershipMapperTest {

    @Autowired
    private MembershipMapper mapper;

    private final EasyRandom generator = new EasyRandom();

    private MembershipDto dto;

    private Membership model;

    @BeforeEach
    public void setup() {
        dto = generator.nextObject(MembershipDto.class);
        model = generator.nextObject(Membership.class);
    }

    @Test
    public void shouldReturnNullWhenConvertingFromNullModel() {
        assertNull(mapper.fromModel(null));
    }

    @Test
    public void shouldReturnDtoWithSameValuesFromModel() {
        final MembershipDto dto = mapper.fromModel(model);
        validateEquals(model, dto);
    }

    @Test
    public void shouldReturnEmptyListWhenConvertingFromNullModelList() {
        assertTrue(mapper.fromModelList(null).isEmpty());
    }

    @Test
    public void shouldReturnDtoListWithSameValuesFromModelList() {
        final List<Membership> modelList = generator.objects(Membership.class, 5).collect(toList());
        final List<MembershipDto> dtoList = mapper.fromModelList(modelList);
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
        final Membership model = mapper.fromDto(dto);
        validateEquals(model, dto);
    }

    @Test
    public void shouldReturnEmptyListWhenConvertingFromNullDtoList() {
        assertTrue(mapper.fromDtoList(null).isEmpty());
    }

    @Test
    public void shouldReturnModelListWithSameValuesFromDtoList() {
        final List<MembershipDto> dtoList = generator.objects(MembershipDto.class, 5).collect(toList());
        final List<Membership> modelList = mapper.fromDtoList(dtoList);
        for (int i = 0; i < modelList.size(); i++) {
            validateEquals(modelList.get(i), dtoList.get(i));
        }
    }

    private void validateEquals(final Membership model, final MembershipDto dto) {
        assertEquals(model.getId(), dto.getId());
        assertEquals(model.getTeamId(), dto.getTeamId());
        assertEquals(model.getUserId(), dto.getUserId());
        assertEquals(model.getRole().getId(), dto.getRoleId());
    }

}
