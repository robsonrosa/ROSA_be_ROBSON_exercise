package com.ecore.roles.web;

import com.ecore.roles.exception.ErrorResponse;
import com.ecore.roles.web.dto.RoleDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.UUID;

@Tag(name = "Role API", description = "API for managing roles for e-core exercise")
public interface RolesApi {

    @Operation(summary = "Create a role", description = "Returns a single role with the specified name")
    @ApiResponse(responseCode = "201", description = "Successful creation of a role",
            content = @Content(schema = @Schema(implementation = RoleDto.class)))
    @ApiResponse(responseCode = "400", description = "Provided role or name already exists",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    ResponseEntity<RoleDto> createRole(RoleDto role);

    @Operation(summary = "Find role by uuid", description = "Returns a single role with the specified uuid")
    @ApiResponse(responseCode = "200", description = "Successful retrieval of a role",
            content = @Content(schema = @Schema(implementation = RoleDto.class)))
    @ApiResponse(responseCode = "404", description = "Role not found with provided uuid",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    ResponseEntity<RoleDto> getRole(UUID roleId);

    @Operation(summary = "Search role by user and team",
            description = "Returns list of roles with provided user and team")
    @ApiResponse(responseCode = "200", description = "Successful retrieval of a role",
            content = @Content(array = @ArraySchema(schema = @Schema(implementation = RoleDto.class))))
    @ApiResponse(responseCode = "400", description = "User or team was not specified",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    ResponseEntity<List<RoleDto>> searchRole(UUID userId, UUID teamId);

    @Operation(summary = "List all roles", description = "Returns a list of all roles in the system")
    @ApiResponse(responseCode = "200", description = "Successful retrieval of all roles",
            content = @Content(array = @ArraySchema(schema = @Schema(implementation = RoleDto.class))))
    ResponseEntity<List<RoleDto>> getRoles();

}
