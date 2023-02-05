package com.ecore.roles.mapper;

import com.ecore.roles.client.model.User;
import com.ecore.roles.client.model.User;
import com.ecore.roles.web.dto.UserDto;
import com.ecore.roles.web.dto.UserDto;
import org.jeasy.random.EasyRandom;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static java.util.stream.Collectors.toList;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class UserMapperTest {

    @Autowired
    private UserMapper mapper;

    private final EasyRandom generator = new EasyRandom();

    private UserDto dto;

    private User model;

    @BeforeEach
    public void setup() {
        dto = generator.nextObject(UserDto.class);
        model = generator.nextObject(User.class);
    }

    @Test
    public void shouldReturnNullWhenConvertingFromNullModel() {
        assertNull(mapper.fromModel(null));
    }

    @Test
    public void shouldReturnDtoWithSameValuesFromModel() {
        final UserDto dto = mapper.fromModel(model);
        validateEquals(model, dto);
    }

    @Test
    public void shouldReturnEmptyListWhenConvertingFromNullModelList() {
        assertTrue(mapper.fromModelList(null).isEmpty());
    }

    @Test
    public void shouldReturnDtoListWithSameValuesFromModelList() {
        final List<User> modelList = generator.objects(User.class, 5).collect(toList());
        final List<UserDto> dtoList = mapper.fromModelList(modelList);
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
        final User model = mapper.fromDto(dto);
        validateEquals(model, dto);
    }

    @Test
    public void shouldReturnEmptyListWhenConvertingFromNullDtoList() {
        assertTrue(mapper.fromDtoList(null).isEmpty());
    }

    @Test
    public void shouldReturnModelListWithSameValuesFromDtoList() {
        final List<UserDto> dtoList = generator.objects(UserDto.class, 5).collect(toList());
        final List<User> modelList = mapper.fromDtoList(dtoList);
        for (int i = 0; i < modelList.size(); i++) {
            validateEquals(modelList.get(i), dtoList.get(i));
        }
    }

    private void validateEquals(final User user, final UserDto dto) {
        assertEquals(user.getId(), dto.getId());
        assertEquals(user.getDisplayName(), dto.getDisplayName());
        assertEquals(user.getFirstName(), dto.getFirstName());
        assertEquals(user.getLastName(), dto.getLastName());
        assertEquals(user.getLastName(), dto.getLastName());
        assertEquals(user.getLastName(), dto.getLastName());
    }

}
