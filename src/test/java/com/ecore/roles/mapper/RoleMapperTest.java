package com.ecore.roles.mapper;

import com.ecore.roles.model.Role;
import com.ecore.roles.web.dto.RoleDto;
import org.jeasy.random.EasyRandom;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static java.util.stream.Collectors.toList;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class RoleMapperTest {

    @Autowired
    private RoleMapper mapper;

    private final EasyRandom generator = new EasyRandom();

    private RoleDto dto;

    private Role model;

    @BeforeEach
    public void setup() {
        dto = generator.nextObject(RoleDto.class);
        model = generator.nextObject(Role.class);
    }

    @Test
    public void shouldReturnNullWhenConvertingFromNullModel() {
        assertNull(mapper.fromModel(null));
    }

    @Test
    public void shouldReturnDtoWithSameValuesFromModel() {
        final RoleDto dto = mapper.fromModel(model);
        validateEquals(model, dto);
    }

    @Test
    public void shouldReturnEmptyListWhenConvertingFromNullModelList() {
        assertTrue(mapper.fromModelList(null).isEmpty());
    }

    @Test
    public void shouldReturnDtoListWithSameValuesFromModelList() {
        final List<Role> modelList = generator.objects(Role.class, 5).collect(toList());
        final List<RoleDto> dtoList = mapper.fromModelList(modelList);
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
        final Role model = mapper.fromDto(dto);
        validateEquals(model, dto);
    }

    @Test
    public void shouldReturnEmptyListWhenConvertingFromNullDtoList() {
        assertTrue(mapper.fromDtoList(null).isEmpty());
    }

    @Test
    public void shouldReturnModelListWithSameValuesFromDtoList() {
        final List<RoleDto> dtoList = generator.objects(RoleDto.class, 5).collect(toList());
        final List<Role> modelList = mapper.fromDtoList(dtoList);
        for (int i = 0; i < modelList.size(); i++) {
            validateEquals(modelList.get(i), dtoList.get(i));
        }
    }

    private void validateEquals(final Role role, final RoleDto dto) {
        assertEquals(role.getId(), dto.getId());
        assertEquals(role.getName(), dto.getName());
    }

}
