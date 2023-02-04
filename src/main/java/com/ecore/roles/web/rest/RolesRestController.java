package com.ecore.roles.web.rest;

import com.ecore.roles.mapper.RoleMapper;
import com.ecore.roles.service.RolesService;
import com.ecore.roles.web.RolesApi;
import com.ecore.roles.web.dto.RoleDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.UUID;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.http.ResponseEntity.ok;

@RequiredArgsConstructor
@RestController
@RequestMapping(value = "/v1/roles", consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
public class RolesRestController implements RolesApi {

    private final RolesService service;

    private final RoleMapper mapper;

    @Override
    @PostMapping
    public ResponseEntity<RoleDto> createRole(@Valid @RequestBody final RoleDto dto) {
        return ResponseEntity
                .status(CREATED)
                .body(mapper.fromModel(service.CreateRole(mapper.fromDto(dto))));
    }

    @Override
    @GetMapping(path = "/{id}")
    public ResponseEntity<RoleDto> getRole(@PathVariable final UUID id) {
        return ok(mapper.fromModel(service.GetRole(id)));
    }

    @Override
    @GetMapping
    public ResponseEntity<List<RoleDto>> getRoles() {
        return ok(mapper.fromModelList(service.GetRoles()));
    }

}
