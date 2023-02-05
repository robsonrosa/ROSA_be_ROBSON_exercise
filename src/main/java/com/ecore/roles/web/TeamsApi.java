package com.ecore.roles.web;

import com.ecore.roles.web.dto.TeamDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.UUID;

@Tag(name = "Team API", description = "API for managing teams for e-core exercise")
public interface TeamsApi {

    @Operation(summary = "List all teams", description = "Returns a list of all teams in the system")
    @ApiResponse(responseCode = "200", description = "Successful retrieval of all teams",
            content = @Content(array = @ArraySchema(schema = @Schema(implementation = TeamDto.class))))
    ResponseEntity<List<TeamDto>> getTeams();

    @Operation(summary = "Find team by uuid", description = "Returns a single team with the specified uuid")
    @ApiResponse(responseCode = "200", description = "Successful retrieval of a team",
            content = @Content(schema = @Schema(implementation = TeamDto.class)))
    @ApiResponse(responseCode = "404", description = "Team not found with provided uuid")
    ResponseEntity<TeamDto> getTeam(
            @Parameter(description = "The uuid of the team to find", required = true) UUID teamId);

}
