package com.ecore.roles.web;

import com.ecore.roles.exception.ErrorResponse;
import com.ecore.roles.web.dto.UserDto;
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

@Tag(name = "User API", description = "API for managing users for e-core exercise")
public interface UsersApi {

    @Operation(summary = "List all users", description = "Returns a list of all users in the system")
    @ApiResponse(responseCode = "200", description = "Successful retrieval of all users",
            content = @Content(array = @ArraySchema(schema = @Schema(implementation = UserDto.class))))
    ResponseEntity<List<UserDto>> getUsers();

    @Operation(summary = "Find user by uuid", description = "Returns a single user with the specified uuid")
    @ApiResponse(responseCode = "200", description = "Successful retrieval of a user",
            content = @Content(schema = @Schema(implementation = UserDto.class)))
    @ApiResponse(responseCode = "404", description = "User not found with provided uuid",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    ResponseEntity<UserDto> getUser(
            @Parameter(description = "The uuid of the user to find", required = true) UUID userId);
}
