package com.ecore.roles.web.rest;

import com.ecore.roles.mapper.UserDtoMapper;
import com.ecore.roles.service.UsersService;
import com.ecore.roles.web.UsersApi;
import com.ecore.roles.web.dto.UserDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RequiredArgsConstructor
@RestController
@RequestMapping(value = "/v1/users", produces = APPLICATION_JSON_VALUE)
public class UsersRestController implements UsersApi {

    private final UsersService usersService;

    private final UserDtoMapper dtoMapper;

    @Override
    @GetMapping
    public ResponseEntity<List<UserDto>> getUsers() {
        return ResponseEntity
                .status(OK)
                .body(dtoMapper.applyList(usersService.getUsers()));
    }

    @Override
    @GetMapping(path = "/{id}")
    public ResponseEntity<UserDto> getUser(@PathVariable final UUID id) {
        return ResponseEntity
                .status(OK)
                .body(dtoMapper.apply(usersService.getUser(id)));
    }
}
