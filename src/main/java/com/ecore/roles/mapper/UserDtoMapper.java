package com.ecore.roles.mapper;

import com.ecore.roles.client.model.User;
import com.ecore.roles.web.dto.UserDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import static java.util.Optional.ofNullable;

@RequiredArgsConstructor
@Component
public class UserDtoMapper implements Mapper<User, UserDto> {

    private final ObjectMapper mapper;

    @Override
    public UserDto apply(final User user) {
        return ofNullable(user)
                .map(this::convert)
                .orElse(null);
    }

    private UserDto convert(final User user) {
        return mapper.convertValue(user, UserDto.class);
    }
}
