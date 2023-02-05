package com.ecore.roles.web.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
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
@Schema(description = "A membership in the system")
public class MembershipDto implements Serializable {

    private static final long serialVersionUID = 6320871070017871416L;

    @JsonProperty
    @Schema(description = "The uuid of the membership", example = "98de61a0-b9e3-11ec-8422-0242ac120002")
    private UUID id;

    @JsonProperty
    @NotNull
    @EqualsAndHashCode.Include
    @Schema(description = "The uuid of the role", example = "1b3c333b-36e7-4b64-aa15-c22ed5908ce4")
    private UUID roleId;

    @JsonProperty
    @NotNull
    @EqualsAndHashCode.Include
    @Schema(description = "The uuid of the user", example = "fd282131-d8aa-4819-b0c8-d9e0bfb1b75c")
    private UUID userId;

    @JsonProperty
    @NotNull
    @EqualsAndHashCode.Include
    @Schema(description = "The uuid of the team", example = "7676a4bf-adfe-415c-941b-1739af07039b")
    private UUID teamId;

}
