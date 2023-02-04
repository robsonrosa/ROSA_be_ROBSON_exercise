package com.ecore.roles.mapper;

import com.ecore.roles.model.Role;
import com.ecore.roles.web.dto.RoleDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Getter
@RequiredArgsConstructor
@Component
public class RoleMapper extends AbstractMapper<Role, RoleDto> {

    private final ObjectMapper mapper;

}
