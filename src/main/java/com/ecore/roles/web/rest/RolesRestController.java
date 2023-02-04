package com.ecore.roles.web.rest;

import com.ecore.roles.model.Role;
import com.ecore.roles.service.RolesService;
import com.ecore.roles.web.RolesApi;
import com.ecore.roles.web.dto.RoleDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static com.ecore.roles.web.dto.RoleDto.fromModel;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RequiredArgsConstructor
@RestController
@RequestMapping(value = "/v1/roles",consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
public class RolesRestController implements RolesApi {

    private final RolesService service;

    @Override
    @PostMapping
    public ResponseEntity<RoleDto> createRole(@Valid @RequestBody final RoleDto role) {
        return ResponseEntity
                .status(CREATED)
                .body(fromModel(service.CreateRole(role.toModel())));
    }

    @Override
    @GetMapping(path = "/{id}")
    public ResponseEntity<RoleDto> getRole(@PathVariable final UUID id) {
        return ResponseEntity
                .status(OK)
                .body(fromModel(service.GetRole(id)));
    }

    @Override
    @GetMapping
    public ResponseEntity<List<RoleDto>> getRoles() {
        final List<Role> getRoles = service.GetRoles();
        final List<RoleDto> roleDtoList = new ArrayList<>();

        for (Role role : getRoles) {
            RoleDto roleDto = fromModel(role);
            roleDtoList.add(roleDto);
        }

        return ResponseEntity
                .status(OK)
                .body(roleDtoList);
    }

}
