package com.ecore.roles.mapper;

import com.ecore.roles.client.model.User;
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
class UserDtoMapperTest {

    @Autowired
    private UserDtoMapper mapper;

    private EasyRandom generator = new EasyRandom();

    private User user;

    @BeforeEach
    public void setup() {
        user = generator.nextObject(User.class);
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
        final UserDto dto = mapper.apply(user);
        validateEquals(user, dto);
    }

    @Test
    public void shouldReturnDtoListWithSameValues() {
        final List<User> entityList = generator.objects(User.class, 5).collect(toList());
        final List<UserDto> dtoList = mapper.applyList(entityList);
        for (int i = 0; i < entityList.size(); i++) {
            validateEquals(entityList.get(i), dtoList.get(i));
        }
    }

    private void validateEquals(final User user, final UserDto dto) {
        assertEquals(user.getId(), dto.getId());
        assertEquals(user.getLocation(), dto.getLocation());
        assertEquals(user.getLastName(), dto.getLastName());
        assertEquals(user.getFirstName(), dto.getFirstName());
        assertEquals(user.getDisplayName(), dto.getDisplayName());
        assertEquals(user.getAvatarUrl(), dto.getAvatarUrl());
    }

}
