package com.ecore.roles.web.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder(toBuilder = true)
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class MembershipDto implements Serializable {

    private static final long serialVersionUID = 6320871070017871416L;

    @JsonProperty
    private UUID id;

    @JsonProperty
    @NotNull
    @EqualsAndHashCode.Include
    private UUID roleId;

    @JsonProperty
    @NotNull
    @EqualsAndHashCode.Include
    private UUID userId;

    @JsonProperty
    @NotNull
    @EqualsAndHashCode.Include
    private UUID teamId;

}
