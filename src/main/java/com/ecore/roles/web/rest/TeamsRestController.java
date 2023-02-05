package com.ecore.roles.web.rest;

import com.ecore.roles.mapper.TeamMapper;
import com.ecore.roles.service.TeamsService;
import com.ecore.roles.web.TeamsApi;
import com.ecore.roles.web.dto.TeamDto;
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
@RequestMapping(value = "/v1/teams", produces = APPLICATION_JSON_VALUE)
public class TeamsRestController implements TeamsApi {

    private final TeamsService service;

    private final TeamMapper mapper;

    @Override
    @GetMapping
    public ResponseEntity<List<TeamDto>> getTeams() {
        return ResponseEntity
                .status(OK)
                .body(mapper.fromModelList(service.getTeams()));
    }

    @Override
    @GetMapping(path = "/{id}")
    public ResponseEntity<TeamDto> getTeam(@PathVariable final UUID id) {
        return ResponseEntity
                .status(OK)
                .body(mapper.fromModel(service.getTeam(id)));
    }

}
