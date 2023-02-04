package com.ecore.roles.web.rest;

import com.ecore.roles.model.Membership;
import com.ecore.roles.service.MembershipsService;
import com.ecore.roles.web.MembershipsApi;
import com.ecore.roles.web.dto.MembershipDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static com.ecore.roles.web.dto.MembershipDto.fromModel;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RequiredArgsConstructor
@RestController
@RequestMapping(value = "/v1/roles/{roleId}/memberships", produces = APPLICATION_JSON_VALUE)
public class MembershipsRestController implements MembershipsApi {

    private final MembershipsService service;

    @Override
    @PostMapping(consumes = APPLICATION_JSON_VALUE)
    public ResponseEntity<MembershipDto> assignRoleToMembership(
            @PathVariable final UUID roleId,
            @NotNull @Valid @RequestBody final MembershipDto dto) {
        dto.setRoleId(roleId);
        final Membership membership = service.assignRoleToMembership(dto.toModel());
        return ResponseEntity
                .status(OK)
                .body(fromModel(membership));
    }

    @Override
    @GetMapping
    public ResponseEntity<List<MembershipDto>> getMemberships(
            @PathVariable final UUID roleId) {

        final List<Membership> memberships = service.getMemberships(roleId);

        final List<MembershipDto> newMembershipDto = new ArrayList<>();

        for (Membership membership : memberships) {
            MembershipDto membershipDto = fromModel(membership);
            newMembershipDto.add(membershipDto);
        }

        return ResponseEntity
                .status(OK)
                .body(newMembershipDto);
    }

}
