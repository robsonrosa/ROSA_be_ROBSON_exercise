package com.ecore.roles.web.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder(toBuilder = true)
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class MembershipDto {

    @JsonProperty
    private UUID id;

    @JsonProperty
    @Valid
    @NotNull
    @EqualsAndHashCode.Include
    private UUID roleId;

    @JsonProperty
    @Valid
    @NotNull
    @EqualsAndHashCode.Include
    private UUID userId;

    @JsonProperty
    @Valid
    @NotNull
    @EqualsAndHashCode.Include
    private UUID teamId;

}
