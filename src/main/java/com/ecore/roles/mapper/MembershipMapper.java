package com.ecore.roles.mapper;

import com.ecore.roles.model.Membership;
import com.ecore.roles.model.Role;
import com.ecore.roles.web.dto.MembershipDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import static java.util.Optional.ofNullable;

@Getter
@RequiredArgsConstructor
@Component
public class MembershipMapper extends AbstractMapper<Membership, MembershipDto> {

    private final ObjectMapper mapper;

    @Override
    public Membership fromDto(final MembershipDto dto) {
        return ofNullable(super.fromDto(dto))
                .map(value -> value.toBuilder()
                        .role(Role
                                .builder()
                                .id(dto.getRoleId())
                                .build())
                        .build())
                .orElse(null);
    }

    @Override
    public MembershipDto fromModel(final Membership model) {
        return ofNullable(super.fromModel(model))
                .map(value -> value.toBuilder()
                        .roleId(ofNullable(model.getRole())
                                .map(Role::getId)
                                .orElse(null))
                        .build())
                .orElse(null);
    }

}
