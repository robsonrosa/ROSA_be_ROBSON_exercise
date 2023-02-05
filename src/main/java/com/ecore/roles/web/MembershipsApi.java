package com.ecore.roles.web;

import com.ecore.roles.exception.ErrorResponse;
import com.ecore.roles.web.dto.MembershipDto;
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

@Tag(name = "Membership API", description = "API for managing memberships for e-core exercise")
public interface MembershipsApi {

    @Operation(summary = "Assign a role to membership",
            description = "Returns a new membership with the specified role")
    @ApiResponse(responseCode = "201", description = "Successful creation of a membership",
            content = @Content(schema = @Schema(implementation = MembershipDto.class)))
    @ApiResponse(responseCode = "400", description = "Provided membership already exists",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    @ApiResponse(responseCode = "400", description = "Provided user does not belong the team",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    @ApiResponse(responseCode = "404", description = "Provided { role | team | user } not found",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    ResponseEntity<MembershipDto> assignRoleToMembership(
            @Parameter(description = "The uuid of the role to assign the membership",
                    required = true) UUID roleId,
            @Parameter(
                    description = "The model of membership to assign with team and user id (the role id will be overwrote by the path variable)",
                    required = true) MembershipDto membership);

    @Operation(summary = "Find memberships by its role id",
            description = "Returns a list of memberships within the provided role id")
    @ApiResponse(responseCode = "200", description = "Successful retrieval of memberships",
            content = @Content(array = @ArraySchema(schema = @Schema(implementation = MembershipDto.class))))
    ResponseEntity<List<MembershipDto>> getMemberships(
            @Parameter(description = "The uuid of the role to find memberships",
                    required = true) UUID roleId);

}
