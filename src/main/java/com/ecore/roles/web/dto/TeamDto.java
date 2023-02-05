package com.ecore.roles.web.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;
import java.util.UUID;

@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
@Schema(description = "A team in the system")
public class TeamDto implements Serializable {

    private static final long serialVersionUID = -669866682933675033L;

    @JsonProperty
    @Schema(description = "The uuid of the team", example = "7676a4bf-adfe-415c-941b-1739af07039b")
    private UUID id;

    @JsonProperty
    @Schema(description = "The first name of the team", example = "Ordinary Coral Lynx")
    private String name;

    @JsonProperty
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @Schema(description = "The uuid of the team's leader", example = "b12fa35a-9c4c-4bf9-8f32-27cf03a1f190")
    private UUID teamLeadId;

    @JsonProperty
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @Schema(description = "The uuid of each member of the team",
            example = "[\"fd282131-d8aa-4819-b0c8-d9e0bfb1b75c\"]")
    private List<UUID> teamMemberIds;

}
