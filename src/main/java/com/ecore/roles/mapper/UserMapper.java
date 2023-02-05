package com.ecore.roles.mapper;

import com.ecore.roles.client.model.User;
import com.ecore.roles.web.dto.UserDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class UserMapper extends AbstractMapper<User, UserDto> {

    private final ObjectMapper mapper;

    @Override
    protected ObjectMapper getMapper() {
        return mapper;
    }
}
