package com.ecore.roles.web.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;
import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder(toBuilder = true)
@EqualsAndHashCode
@Schema(description = "A role in the system")
public class RoleDto implements Serializable {

    private static final long serialVersionUID = -6495601927119593664L;

    @JsonProperty
    @Schema(description = "The uuid of the role", example = "1b3c333b-36e7-4b64-aa15-c22ed5908ce4")
    private UUID id;
    @JsonProperty
    @NotBlank
    @Schema(description = "The first name of the role", example = "Developer")
    private String name;

}
