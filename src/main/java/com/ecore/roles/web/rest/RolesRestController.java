package com.ecore.roles.web.rest;

import com.ecore.roles.mapper.RoleMapper;
import com.ecore.roles.service.RolesService;
import com.ecore.roles.web.RolesApi;
import com.ecore.roles.web.dto.RoleDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import java.util.List;
import java.util.UUID;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.http.ResponseEntity.ok;

@RequiredArgsConstructor
@RestController
@RequestMapping(value = "/v1/roles", produces = APPLICATION_JSON_VALUE)
public class RolesRestController implements RolesApi {

    private final RolesService service;

    private final RoleMapper mapper;

    @Override
    @PostMapping(consumes = APPLICATION_JSON_VALUE)
    public ResponseEntity<RoleDto> createRole(@Valid @RequestBody final RoleDto dto) {
        return ResponseEntity
                .status(CREATED)
                .body(mapper.fromModel(service.createRole(mapper.fromDto(dto))));
    }

    @Override
    @GetMapping
    public ResponseEntity<List<RoleDto>> getRoles() {
        return ok(mapper.fromModelList(service.getRoles()));
    }

    @Override
    @GetMapping(path = "/{id}")
    public ResponseEntity<RoleDto> getRole(@PathVariable final UUID id) {
        return ok(mapper.fromModel(service.getRole(id)));
    }

    @Override
    @GetMapping(path = "/search")
    public ResponseEntity<List<RoleDto>> searchRole(
            @NotBlank @RequestParam final UUID userId,
            @NotBlank @RequestParam final UUID teamId) {

        return ok(mapper.fromModelList(service.searchRole(userId, teamId)));
    }

}
